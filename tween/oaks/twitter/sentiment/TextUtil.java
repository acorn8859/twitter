package tween.oaks.twitter.sentiment;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Denis Kotkov
 */
public class TextUtil {
	private final static String DEFAULT_NAME = "Denis";
	private final static String DEFAULT_LINK = "link";
	private final static TreeMap<String, String> REPLACEMENTS = new TreeMap<String, String>();

	static {
		REPLACEMENTS.put("afaik", "as far as I know");
		REPLACEMENTS.put("icymi", "in case you missed it");
		REPLACEMENTS.put("nsfw", "not safe for work");
		REPLACEMENTS.put("tbh", "to be honest");
		REPLACEMENTS.put("imo", "in my opinion");
		REPLACEMENTS.put("til", "today I learned");
		REPLACEMENTS.put("cuz", "because");
		REPLACEMENTS.put("wat", "what");
		REPLACEMENTS.put("gr8", "great");
		REPLACEMENTS.put("lol", "");
        REPLACEMENTS.put("da", "the");
		REPLACEMENTS.put("w/", "with");
		REPLACEMENTS.put("u", "you");
		REPLACEMENTS.put("w", "with");
	}

	public static String reduceNoise(String text) {
		String nameReplaced = replaceName(text);
		String hashTagReplaced = replaceHashTag(nameReplaced);
		String uriReplaces = replaceURI(hashTagReplaced);
		String chatReplaced = replaceChatWords(uriReplaces);
		String cleanText = clearText(chatReplaced);
		return cleanText;
	}

	private static String replaceName(String text) {
		String pattern = "@([A-Za-z0-9_]+)";
		return text.replaceAll(pattern, DEFAULT_NAME);
	}

	private static String replaceHashTag(String text) {
		String pattern = "#([A-Za-z0-9_]+)";
		return text.replaceAll(pattern, "");
	}

	private static String replaceURI(String text) {
		String pattern = "[a-zA-Z][a-zA-Z0-9\\+\\-\\.]*:((((//((((([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\);:\\&=\\+$,]|(%[a-fA-F0-9]{2}))*)\\@)?((((([a-zA-Z0-9](([a-zA-Z0-9\\-])*[a-zA-Z0-9])?)\\.)*([a-zA-Z](([a-zA-Z0-9\\-])*[a-zA-Z0-9])?)(\\.)?)|([0-9]+((\\.[0-9]+){3})))(:[0-9]*)?))?|([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\)$,;:\\@\\&=\\+]|(%[a-fA-F0-9]{2}))+)(/(([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\):\\@\\&=\\+$,]|(%[a-fA-F0-9]{2}))*(;([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\):\\@\\&=\\+$,]|(%[a-fA-F0-9]{2}))*)*)(/(([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\):\\@\\&=\\+$,]|(%[a-fA-F0-9]{2}))*(;([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\):\\@\\&=\\+$,]|(%[a-fA-F0-9]{2}))*)*))*)?)|(/(([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\):\\@\\&=\\+$,]|(%[a-fA-F0-9]{2}))*(;([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\):\\@\\&=\\+$,]|(%[a-fA-F0-9]{2}))*)*)(/(([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\):\\@\\&=\\+$,]|(%[a-fA-F0-9]{2}))*(;([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\):\\@\\&=\\+$,]|(%[a-fA-F0-9]{2}))*)*))*))(\\?([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\);/\\?:\\@\\&=\\+$,]|(%[a-fA-F0-9]{2}))*)?)|(([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\);\\?:\\@\\&=\\+$,]|(%[a-fA-F0-9]{2}))([a-zA-Z0-9\\-_\\.!\\~\\*'\\(\\);/\\?:\\@\\&=\\+$,]|(%[a-fA-F0-9]{2}))*))";
		return text.replaceAll(pattern, DEFAULT_LINK);
	}

	private static String replaceChatWords(String text) {
		for (Map.Entry<String, String> entry : REPLACEMENTS.entrySet()) {
			Pattern pattern = Pattern.compile("(?i)(\\W|^)" + entry.getKey() + "(\\W|$)");
			Matcher matcher = pattern.matcher(text);
            String replacement;
			while (matcher.find()) {
                replacement = matcher.group().replaceAll(entry.getKey(), entry.getValue());
                text = text.replaceAll(matcher.group(), replacement);
			}
		}
		return text;
	}

	private static String clearText(String text) {
		Pattern pattern = Pattern.compile("[a-zA-Z'\\(\\)\\[\\]0-9!,;:\\?\\.]+");
		Matcher matcher = pattern.matcher(text);
		StringBuilder builder = new StringBuilder();
		while (matcher.find()) {
			builder.append(matcher.group());
			builder.append(" ");
		}
		return builder.toString();
	}

	public static String normalizeSource(String source) {
		Pattern pattern = Pattern.compile(">.*<");
		Matcher matcher = pattern.matcher(source);
		if (matcher.find()) {
			StringBuilder builder = new StringBuilder(matcher.group());
			builder.deleteCharAt(0);
			builder.deleteCharAt(builder.length() - 1);
			return builder.toString();
		}
		return null;
	}

    public static boolean consistsPositiveEmoticon(String text){
        Pattern pattern = Pattern.compile(":\\)|:\\]|:\\}|=\\)|=\\]|=\\}");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static boolean consistsNegativeEmoticon(String text){
        Pattern pattern = Pattern.compile(":\\(|:\\[|:\\{|=\\(|=\\[|=\\{");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
}
