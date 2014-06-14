package tween.oaks.twitter.sentiment;

import org.apache.log4j.Logger;
import tween.oaks.twitter.sentiment.twitter.TweetBuilder;
import tween.oaks.twitter.sentiment.twitter.TweetStat;
import tween.oaks.twitter.sentiment.twitter.TwitterFeed;
import tween.oaks.twitter.sentiment.twitter.TwitterFeedListener;
import twitter4j.*;

import java.util.Date;
import java.util.List;

/**
 * @author Denis Kotkov
 */
public class Controller {
    private static String FILTER_COUNTRY = "United States";

    private TwitterFeed twitterFeed;
    private TextAnalyzer analyzer;
    private FeedListener listener;
    private DBManager dbManager;
    private final Logger logger = Logger.getLogger(Controller.class);
    private boolean run;
    private static Controller instance;

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    private Controller() {
        twitterFeed = new TwitterFeed();
        analyzer = new TextAnalyzer();
        listener = new FeedListener();
        dbManager = DBManager.getInstance();
    }

    public void subscribe() {
        dbManager.connect();
        twitterFeed.addListener(listener);
        twitterFeed.subscribe();
        logger.info("Start");
        run = true;
    }

    public void unsubscribe() {
        dbManager.disconnect();
        instance = null;
        run = false;
    }

    public boolean isRun() {
        return run;
    }

    private class FeedListener implements TwitterFeedListener {
        public void tweetReceived(Status status) {
            logger.info("Got " + status.getId());
            try {
                storeData(status);
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
            }
        }

        private void storeData(Status status) {
            if (status == null || status.getText() == null || status.getText().isEmpty()) {
                return;
            }
            Place place = status.getPlace();
            if (place == null || place.getCountry() == null || !place.getCountry().equalsIgnoreCase(FILTER_COUNTRY)) {
                if(place != null && place.getCountry() != null) {
                    System.out.println("Skipped " + place.getCountry());
                }else  {
                    System.out.println("No place");
                }
                return;
            }
            TweetBuilder builder = new TweetBuilder();
            String cleanText = TextUtil.reduceNoise(status.getText());
            builder.setNormText(cleanText);
            Sentiment sentiment;
            if(TextUtil.consistsPositiveEmoticon(status.getText())){
                sentiment = Sentiment.POSITIVE;
            }else if(TextUtil.consistsNegativeEmoticon(status.getText())) {
                sentiment = Sentiment.NEGATIVE;
            }else{
                sentiment = analyzer.analyze(cleanText);
            }
            builder.setSentiment(sentiment);
            builder.setText(status.getText());

            builder.setId(status.getId());

            builder.setPlaceName(place.getName());
            builder.setPlaceType(place.getPlaceType());
            builder.setCountry(place.getCountry());

            User user = status.getUser();
            if (user != null) {
                builder.setUserId(user.getId());
            }

            String source = TextUtil.normalizeSource(status.getSource());
            builder.setSource(source);

            builder.setCreatedAt(status.getCreatedAt());

            dbManager.insertTweet(builder.toTweet());
        }
    }
}
