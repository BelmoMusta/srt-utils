package model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter

public class SrtItem implements Comparable<SrtItem> {
    public static final String REGEX = "(\\d+)\\r?\\n(\\d{2}:\\d{2}:\\d{2},\\d{3}) --> (\\d{2}:\\d{2}:\\d{2},\\d{3})\\r?\\n(.+)";
    private int order;
    private TimeMarker timeMarker;
    private String text;


    public SrtItem shiftForwardBy(int amount, ChronoUnit unit) {
        timeMarker.shiftForwardBy(amount, unit);
        return this;
    }

    public SrtItem shiftBackwardsBy(int amount) {
        timeMarker.shiftBackwardsBy(amount);
        return this;
    }

    public void setEndsAt(LocalTime endsAt) {
        timeMarker.setEndsAt(endsAt);
    }

    public void setStartsAt(LocalTime startsAt) {
        timeMarker.setStartsAt(startsAt);
    }

    public SrtItem startsAt(String startsAtAsString) {
        checkTimeMarker();
        timeMarker.startsAt(startsAtAsString);
        return this;
    }

    public SrtItem endsAt(String endsAtAsString) {
        checkTimeMarker();
        timeMarker.endsAt(endsAtAsString);
        return this;
    }

    private void checkTimeMarker() {
        if (timeMarker == null) {
            timeMarker = new TimeMarker();
        }
    }

    @Override
    public String toString() {
        return order
                + "\n"
                + timeMarker.toString()
                + "\n"
                + text + "\n";
    }

    @Override
    public int compareTo(SrtItem o) {
        return Comparator.comparing(SrtItem::getOrder).compare(this, o);
    }


    public static void main(String[] args) {


        String s = "1\n" +
                "00:00:37,737 --> 00:00:40,296\n" +
                "Technically, what happened wasn't my fault.\n" +
                "\n" +
                "2\n" +
                "00:00:40,306 --> 00:00:41,464\n" +
                "I'm a minor.";

        Set<SrtItem> srtItems = fromString(s);
        srtItems.forEach(System.out::println);

    }

    public static Set<SrtItem> fromString(final String s) {
        final Pattern pattern = Pattern.compile(REGEX);
        final Matcher matcher = pattern.matcher(s);
        final Set<SrtItem> srtItems = new TreeSet<>();

        while (matcher.find()) {
            final String order = matcher.group(1);
            final String startsAt = matcher.group(2);
            final String endsAt = matcher.group(3);
            final String text = matcher.group(4);
            final SrtItem srtItem = create(order, startsAt, endsAt, text);
            srtItems.add(srtItem);
        }
        return srtItems;
    }

    public static SrtItem create(final String order,
                                 final String startsAt,
                                 final String endsAt,
                                 final String text) {
        final SrtItem srtItem = new SrtItem();
        srtItem.setOrder(Integer.parseInt(order));
        srtItem.startsAt(startsAt);
        srtItem.endsAt(endsAt);
        srtItem.setText(text);
        return srtItem;

    }

    public String getStartsAt() {
        return timeMarker.getStartsAt().format(StaticHolder.getFormatter());
    }

    public String getEndsAt() {
        return timeMarker.getEndsAt().format(StaticHolder.getFormatter());
    }
}
