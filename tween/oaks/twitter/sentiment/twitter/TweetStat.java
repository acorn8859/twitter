package tween.oaks.twitter.sentiment.twitter;

import java.util.Date;

/**
 * Created by Bionic1251 on 13.02.14.
 */
public class TweetStat {
    Long count;
    Date date;

    public TweetStat(Long count, Date date) {
        this.count = count;
        this.date = date;
    }

    public Long getCount() {
        return count;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "TS[" + date.toString() + ", " + count + "]";
    }
}
