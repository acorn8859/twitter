package tween.oaks.twitter.sentiment;

/**
 * @author Denis Kotkov
 */
public enum Sentiment {
	POSITIVE("1"), NEUTRAL("0"), NEGATIVE("-1");
	private String name;

	Sentiment(String name) {
		this.name = name;
	}


	@Override
	public String toString() {
		return name;
	}
}
