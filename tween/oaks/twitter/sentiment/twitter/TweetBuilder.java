package tween.oaks.twitter.sentiment.twitter;

import tween.oaks.twitter.sentiment.Sentiment;

import java.util.Date;

public class TweetBuilder {
	private long locId;
	private long id;
	private String text;
	private String normText;
	private Date createdAt;
	private String source;
	private boolean truncated;
	private long replayToStatusId;
	private String country;
	private String placeName;
	private String placeType;
	private long userId;
	private Sentiment sentiment;

	public TweetBuilder() {
	}

	public void setLocId(long locId) {
		this.locId = locId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setNormText(String normText) {
		this.normText = normText;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public void setReplayToStatusId(long replayToStatusId) {
		this.replayToStatusId = replayToStatusId;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public void setPlaceType(String placeType) {
		this.placeType = placeType;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setSentiment(Sentiment sentiment) {
		this.sentiment = sentiment;
	}

	public long getLocId() {
		return locId;
	}

	public long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public String getNormText() {
		return normText;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public String getSource() {
		return source;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public long getReplayToStatusId() {
		return replayToStatusId;
	}

	public String getCountry() {
		return country;
	}

	public String getPlaceName() {
		return placeName;
	}

	public String getPlaceType() {
		return placeType;
	}

	public long getUserId() {
		return userId;
	}

	public Sentiment getSentiment() {
		return sentiment;
	}

	public Tweet toTweet() {
		if (text == null || text.isEmpty() || normText == null) {
			return null;
		}

		return new Tweet(locId, id, text, normText, createdAt, source, truncated, replayToStatusId,
				country, placeName, placeType, userId, sentiment);
	}
}
