package tween.oaks.twitter.sentiment;

import org.apache.log4j.Logger;
import tween.oaks.twitter.sentiment.twitter.Tweet;
import tween.oaks.twitter.sentiment.twitter.TweetStat;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DBManager {
    private static String URL = "";
    private static String USER_NAME = "";
    private static String PASSWORD = "";
    private Connection connection;
    private static DBManager instance;
    Logger logger = Logger.getLogger(DBManager.class);

    private DBManager() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.out.println(e);
            logger.error(e.getMessage(), e);
        }
    }

    public void connect() {
        if (connection != null) {
            return;
        }
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    public void disconnect() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);

        }
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public Long insertTweet(Tweet tweet) {
        if (tweet == null) {
            return null;
        }
        String sql = "INSERT INTO tweet(id, text, norm_text, created_at, source, truncated, " +
                "replay_to_status_id, country, place_name, place_type, " +
                "user_id, sentiment)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?::mood)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, tweet.getId());
            preparedStatement.setString(2, tweet.getText());
            preparedStatement.setString(3, tweet.getNormText());
            preparedStatement.setTimestamp(4, new Timestamp(tweet.getCreatedAt().getTime()));
            preparedStatement.setString(5, tweet.getSource());
            preparedStatement.setBoolean(6, tweet.isTruncated());
            preparedStatement.setLong(7, tweet.getReplayToStatusId());
            preparedStatement.setString(8, tweet.getCountry());
            preparedStatement.setString(9, tweet.getPlaceName());
            preparedStatement.setString(10, tweet.getPlaceType());
            preparedStatement.setLong(11, tweet.getUserId());
            preparedStatement.setString(12, tweet.getSentiment().toString());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong("loc_id");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    private Long getWordId(String word) {
        String sql = "select id from word where text=?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, word.toLowerCase());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    private void insertWordLink(long wordId, int position, long tweetId) {
        String sql = "INSERT INTO tweet_word(tweet_id, word_id, position) VALUES(?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, tweetId);
            preparedStatement.setLong(2, wordId);
            preparedStatement.setInt(3, position);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private String getWordsStatementPart(String[] words) {
        String statement = " and (" + getWordPart(words[0]);
        for(int i = 1; i < words.length; i++) {
            statement += " or " + getWordPart(words[i]);
        }
        return statement + ")";
    }

    private String getWordPart(String word){
        return "text ~* '\\y" + word.toLowerCase() + "\\y'";
    }

    public List<TweetStat> getTweetStats(java.util.Date start, java.util.Date end, Sentiment sentiment, String words[]) {
        String whereStatement = "where Date(created_at) >= ? and Date(created_at) <= ? and sentiment='" + sentiment.toString() + "'";
        if (words.length > 0) {
            whereStatement += getWordsStatementPart(words);
        }
        String sql = "select count(created_at), Date(created_at) from tweet " + whereStatement +
                " group by Date(created_at) order by Date(created_at)";
        System.out.println(sql);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, new Date(start.getTime()));
            preparedStatement.setDate(2, new Date(end.getTime()));
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            List<TweetStat> data = new ArrayList<TweetStat>();
            while (resultSet.next()) {
                data.add(new TweetStat(resultSet.getLong(1), resultSet.getDate(2)));
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }
}
