package tween.oaks.twitter.sentiment.twitter;

import twitter4j.Status;

/**
 * @author Denis Kotkov
 */
public interface TwitterFeedListener {
	void tweetReceived(Status status);
}
