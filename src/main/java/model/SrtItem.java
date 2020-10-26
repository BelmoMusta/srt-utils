package model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter

public class SrtItem implements Comparable<SrtItem> {
	public static final String REGEX = "(\\d+)\\r?\\n(\\d{2}:\\d{2}:\\d{2},\\d{3}) --> (\\d{2}:\\d{2}:\\d{2},\\d{3})" +
			"\\r?\\n(.+\\r?\\n?.*)";
	public static final Function<String, SrtItem> STRING_MAPPER = SrtItem::new;
	private String language;
	private int order;
	private TimeMarker timeMarker;
	private String text;
	private Map<String, String> translations = new HashMap<>();
	
	public SrtItem() {
	}
	
	public SrtItem(String str) {
		Pattern compile = Pattern.compile(REGEX);
		Matcher matcher = compile.matcher(str);
		
		if (matcher.find()) {
			order = Integer.parseInt(matcher.group(1));
			String startsAt = matcher.group(2);
			String endsAt = matcher.group(3);
			timeMarker = new TimeMarker()
					.startsAt(startsAt)
					.endsAt(endsAt);
			text = matcher.group(4);
		}
	}
	
	
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
		return toString(language);
	}
	
	public String toString(String lang) {
		return order
				+ "\n"
				+ timeMarker
				+ "\n"
				+ getText(lang) + "\n";
	}
	
	@Override
	public int compareTo(SrtItem o) {
		return Comparator.comparing(SrtItem::getOrder).compare(this, o);
	}
	
	public String getStartsAt() {
		return timeMarker.getStartsAt().format(StaticHolder.getFormatter());
	}
	
	public String getEndsAt() {
		return timeMarker.getEndsAt().format(StaticHolder.getFormatter());
	}
	
	public String getText(String language) {
		return translations.getOrDefault(language, text);
	}
	
	public String setText(String language, String text) {
		return translations.putIfAbsent(language, text);
	}
}
