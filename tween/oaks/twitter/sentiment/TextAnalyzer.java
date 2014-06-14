package tween.oaks.twitter.sentiment;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;

/**
 * @author Denis Kotkov
 */
public class TextAnalyzer {
	private final StanfordCoreNLP pipeline;

	public TextAnalyzer() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
	}

	public Sentiment analyze(String text) {
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		int result = 0;
		for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
			int normalized = getNormalizedValue(sentiment);
			result += normalized;
		}
		if (result == 0) {
			return Sentiment.NEUTRAL;
		}
		if (result > 0) {
			return Sentiment.POSITIVE;
		}
		return Sentiment.NEGATIVE;
	}

	private int getNormalizedValue(int value) {
		return value - 2;
	}
}
