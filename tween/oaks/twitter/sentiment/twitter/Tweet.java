package tween.oaks.twitter.sentiment.twitter;

import tween.oaks.twitter.sentiment.Sentiment;

import java.util.Date;

public class Tweet {
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

	public Tweet(long locId, long id, String text, String normText, Date createdAt, String source, boolean truncated, long replayToStatusId, String country, String placeName, String placeType, long userId, Sentiment sentiment) {
		this.locId = locId;
		this.id = id;
		this.text = text;
		this.normText = normText;
		this.createdAt = createdAt;
		this.source = source;
		this.truncated = truncated;
		this.replayToStatusId = replayToStatusId;
		this.country = country;
		this.placeName = placeName;
		this.placeType = placeType;
		this.userId = userId;
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

	@Override
	public String toString() {
		return "tween.oaks.twitter.sentiment.Tweet[locId " + locId + ", id " + id + ", text " + text + ", normText " + normText + ", createdAt " +
				createdAt + ", source " + source + ", truncated " + truncated +
				", replayToStatusId " + replayToStatusId + ", country " + country
				+ ", placeName " + placeName + ", placeType " + placeType + ", userId " + userId + ", sentiment " +
				sentiment + "]";
	}
}
