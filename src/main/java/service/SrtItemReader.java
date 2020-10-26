package service;

import model.SrtItem;

import java.util.Set;
import java.util.TreeSet;

import static model.SrtItem.REGEX;

public class SrtItemReader {
    
    public static Set<SrtItem> createItemsFromContent(String content) {
        final Set<SrtItem> srtItemSet;
        try (RegexScanner regexScanner = new RegexScanner(content, REGEX)) {
            srtItemSet = new TreeSet<>();
            while (regexScanner.hasNext()) {
                final SrtItem srtItem = regexScanner.next(SrtItem.STRING_MAPPER);
                srtItemSet.add(srtItem);
            }
        }
        return srtItemSet;
    }
}
