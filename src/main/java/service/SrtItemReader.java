package service;

import model.SrtItem;

import java.util.Set;
import java.util.TreeSet;

import static model.SrtItem.REGEX;

public class SrtItemReader {

    public static Set<SrtItem> fromString(final String s) {
        final RegexScanner regexScanner = new RegexScanner(s, REGEX);
        final Set<SrtItem> srtItems = new TreeSet<>();
        while (regexScanner.hasNext()) {
            SrtItem srtItem = regexScanner.next(SrtItem.STRING_MAPPER);
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
}
