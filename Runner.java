import tween.oaks.twitter.sentiment.Controller;
import tween.oaks.twitter.sentiment.Sentiment;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Denis Kotkov
 */
public class Runner {
	public static void main(String... args) {
		Controller controller = Controller.getInstance();
		controller.subscribe();
        /*Calendar calendar = Calendar.getInstance();
        Date end = calendar.getTime();
        calendar.add(Calendar.WEEK_OF_YEAR, -2);
        Date start = calendar.getTime();
        System.out.println(controller.getStats(start, end, Sentiment.NEGATIVE));*/
	}

}
