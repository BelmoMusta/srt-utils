package model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class TimeMarker {




    private LocalTime startsAt;
    private LocalTime endsAt;

    public TimeMarker startsAt(String startsAtAsString) {
        startsAt = LocalTime.parse(startsAtAsString, StaticHolder.getFormatter());
        return this;
    }

    public TimeMarker endsAt(String endsAtAsString) {
        endsAt = LocalTime.parse(endsAtAsString, StaticHolder.getFormatter());
        return this;
    }

    @Override
    public String toString() {
        return StaticHolder.getFormatter().format(startsAt)
                + " --> "
                + StaticHolder.getFormatter().format(endsAt);
    }

    public TimeMarker shiftForwardBy(int amount, ChronoUnit unit) {
        this.startsAt = startsAt.plus(amount, unit);
        this.endsAt = endsAt.plus(amount, unit);

        return this;
    }

    public TimeMarker shiftForwardBy(int amount) {
        return shiftForwardBy(amount, ChronoUnit.MILLIS);
    }

    public TimeMarker shiftBackwardsBy(int amount) {
        return shiftForwardBy(-amount, ChronoUnit.MILLIS);
    }


}
