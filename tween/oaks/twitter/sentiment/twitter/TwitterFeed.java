package tween.oaks.twitter.sentiment.twitter;

import org.apache.log4j.Logger;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Kotkov
 */
public class TwitterFeed {
    private static String TWITTER_OAUTH_CONSUMER_KEY = "cCc7vNT3PdxGKwgzhsu0UhIiV";
    private static String TWITTER_OAUTH_CONSUMER_SECRET = "4RImIsQWF5uLLITWc3BWmNvRrWogVMzkkNZdgziO0ewUKTZi4F";
    private static String TWITTER_OAUTH_ACCESS_TOKEN = "2425680973-IvnGgoJI5F8rExxzFFLKwEGz68A94mido93skSs";
    private static String TWITTER_OAUTH_ACCESS_TOKEN_SECRET = "jzOJNTebmgbX159FzPTb4SqIYhkysG7Cxk9bOq6MhTKSN";

    private final List<TwitterFeedListener> listeners = new ArrayList<TwitterFeedListener>();
    private Logger logger = Logger.getLogger(TwitterFeed.class);

    public void addListener(TwitterFeedListener l) {
        listeners.add(l);
    }

    public void removeListener(TwitterFeedListener l) {
        listeners.remove(l);
    }

    private void notifyListener(Status status) {
        for (TwitterFeedListener l : listeners) {
            l.tweetReceived(status);
        }
    }

    public void subscribe() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setDebugEnabled(true);
        builder.setOAuthConsumerKey(TWITTER_OAUTH_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TWITTER_OAUTH_CONSUMER_SECRET);
        builder.setOAuthAccessToken(TWITTER_OAUTH_ACCESS_TOKEN);
        builder.setOAuthAccessTokenSecret(TWITTER_OAUTH_ACCESS_TOKEN_SECRET);
        builder.setDispatcherImpl(TweetDispatcher.class.getCanonicalName());
        TwitterStreamFactory factory = new TwitterStreamFactory(builder.build());
        TwitterStream twitterStream = factory.getInstance();
        twitterStream.addListener(new Listener());
        FilterQuery query = new FilterQuery();
        double[][] locations = {{-121.446667, 20.048528}, {-45.450583, 50.171861}};
        query.locations(locations);
        query.language(new String[]{"en"});
        twitterStream.filter(query);
    }

    private class Listener implements StatusListener {
        public void onStatus(Status status) {
            notifyListener(status);
        }

        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            System.out.println("Delete " + statusDeletionNotice.getStatusId());
        }

        public void onTrackLimitationNotice(int i) {
            logger.error("onTrackLimitationNotice " + i);
            System.out.println("onTrackLimitationNotice " + i);
        }

        public void onScrubGeo(long l, long l1) {
            logger.error("onScrubGeo " + l + " " + l1);
            System.out.println("onScrubGeo " + l + " " + l1);
        }

        public void onStallWarning(StallWarning stallWarning) {
            logger.error("onStallWarning " + stallWarning.getMessage());
            System.out.println("onStallWarning " + stallWarning.getMessage());
        }

        public void onException(Exception e) {
            System.out.println("onException");
            e.printStackTrace();
            logger.error(e);
        }
    }
}
