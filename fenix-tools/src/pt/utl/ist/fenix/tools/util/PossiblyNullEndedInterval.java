package pt.utl.ist.fenix.tools.util;

import java.io.Serializable;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.MutableInterval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * This class implements all the API of ReadableInterval but conveys the
 * possibility of being null ended. Most of the API behave exactly as the
 * ReadableInterval, some methods, namely: {@link #getEnd()},
 * {@link #getEndMillis()}, {@link #toInterval()}, {@link #toMutableInterval()},
 * {@link #toDuration()}, {@link #toDurationMillis()}, {@link #toPeriod()},
 * {@link #toPeriod(PeriodType)}, will throw an exception if they are null
 * ended.
 * 
 * @author Pedro Santos
 */
public class PossiblyNullEndedInterval implements Serializable {
    public static class OpenEndedIntervalEndInstantAccessAttempt extends RuntimeException {
	private static final long serialVersionUID = 6714455855595216145L;
    }

    private static final long NULL_END_INSTANT = -1;

    private static final long serialVersionUID = 9210575204515350017L;

    private final long startInstant;

    private final long endInstant;

    private final Chronology chronology;

    public PossiblyNullEndedInterval(long startInstant) {
	this.startInstant = startInstant;
	this.endInstant = NULL_END_INSTANT;
	this.chronology = null;
    }

    public PossiblyNullEndedInterval(long startInstant, long endInstant) {
	this.startInstant = startInstant;
	this.endInstant = endInstant;
	this.chronology = null;
    }

    public PossiblyNullEndedInterval(long startInstant, Chronology chronology) {
	this.startInstant = startInstant;
	this.endInstant = NULL_END_INSTANT;
	this.chronology = chronology;
    }

    public PossiblyNullEndedInterval(long startInstant, long endInstant, Chronology chronology) {
	this.startInstant = startInstant;
	this.endInstant = endInstant;
	this.chronology = chronology;
    }

    public Chronology getChronology() {
	return chronology;
    }

    public DateTime getStart() {
	return new DateTime(getStartMillis(), getChronology());
    }

    public long getStartMillis() {
	return startInstant;
    }

    public DateTime getEnd() {
	return new DateTime(getEndMillis(), getChronology());
    }

    public long getEndMillis() {
	if (endInstant == NULL_END_INSTANT)
	    throw new OpenEndedIntervalEndInstantAccessAttempt();
	return endInstant;
    }

    public boolean contains(long millisInstant) {
	return millisInstant >= getStartMillis();
    }

    public boolean containsNow() {
	return contains(DateTimeUtils.currentTimeMillis());
    }

    public boolean contains(ReadableInstant instant) {
	if (instant == null) {
	    return containsNow();
	}
	return contains(instant.getMillis());
    }

    public boolean contains(ReadableInterval interval) {
	if (interval == null) {
	    return containsNow();
	}
	return contains(interval.getStartMillis());
    }

    public boolean contains(PossiblyNullEndedInterval interval) {
	if (interval == null) {
	    return containsNow();
	}
	return contains(interval.getStartMillis());
    }

    public boolean overlaps(ReadableInterval interval) {
	if (interval == null) {
	    return getStartMillis() < DateTimeUtils.currentTimeMillis();
	} else {
	    return getStartMillis() < interval.getEndMillis();
	}
    }

    public boolean overlaps(PossiblyNullEndedInterval interval) {
	if (interval == null) {
	    return getStartMillis() < DateTimeUtils.currentTimeMillis();
	} else {
	    return getStartMillis() < interval.getEndMillis();
	}
    }

    public boolean isBefore(long millisInstant) {
	if (endInstant == NULL_END_INSTANT)
	    return false;
	return (getEndMillis() <= millisInstant);
    }

    public boolean isBeforeNow() {
	return isBefore(DateTimeUtils.currentTimeMillis());
    }

    public boolean isBefore(ReadableInstant instant) {
	if (instant == null) {
	    return isBeforeNow();
	}
	return isBefore(instant.getMillis());
    }

    public boolean isBefore(ReadableInterval interval) {
	if (interval == null) {
	    return isBeforeNow();
	}
	return isBefore(interval.getStartMillis());
    }

    public boolean isBefore(PossiblyNullEndedInterval interval) {
	if (interval == null) {
	    return isBeforeNow();
	}
	return isBefore(interval.getStartMillis());
    }

    public boolean isAfter(long millisInstant) {
	return (getStartMillis() > millisInstant);
    }

    public boolean isAfterNow() {
	return isAfter(DateTimeUtils.currentTimeMillis());
    }

    public boolean isAfter(ReadableInstant instant) {
	if (instant == null) {
	    return isAfterNow();
	}
	return isAfter(instant.getMillis());
    }

    public boolean isAfter(ReadableInterval interval) {
	long endMillis;
	if (interval == null) {
	    endMillis = DateTimeUtils.currentTimeMillis();
	} else {
	    endMillis = interval.getEndMillis();
	}
	return (getStartMillis() >= endMillis);
    }

    public boolean isAfter(PossiblyNullEndedInterval interval) {
	long endMillis;
	if (interval == null) {
	    endMillis = DateTimeUtils.currentTimeMillis();
	} else {
	    endMillis = interval.getEndMillis();
	}
	if (endMillis == NULL_END_INSTANT)
	    return false;
	return (getStartMillis() >= endMillis);
    }

    public Interval toInterval() {
	return new Interval(getStartMillis(), getEndMillis(), getChronology());
    }

    public MutableInterval toMutableInterval() {
	return new MutableInterval(getStartMillis(), getEndMillis(), getChronology());
    }

    public long toDurationMillis() {
	return FieldUtils.safeAdd(getEndMillis(), -getStartMillis());
    }

    public Duration toDuration() {
	long durMillis = toDurationMillis();
	if (durMillis == 0) {
	    return Duration.ZERO;
	} else {
	    return new Duration(durMillis);
	}
    }

    public Period toPeriod() {
	return new Period(getStartMillis(), getEndMillis(), getChronology());
    }

    public Period toPeriod(PeriodType type) {
	return new Period(getStartMillis(), getEndMillis(), type, getChronology());
    }

    @Override
    public boolean equals(Object readableInterval) {
	if (this == readableInterval) {
	    return true;
	}
	if (!(readableInterval instanceof PossiblyNullEndedInterval)) {
	    return false;
	}
	PossiblyNullEndedInterval other = (PossiblyNullEndedInterval) readableInterval;
	return startInstant == other.startInstant && endInstant == other.endInstant
		&& FieldUtils.equals(getChronology(), other.getChronology());
    }

    @Override
    public int hashCode() {
	long start = startInstant;
	long end = endInstant;
	int result = 97;
	result = 31 * result + ((int) (start ^ (start >>> 32)));
	result = 31 * result + ((int) (end ^ (end >>> 32)));
	result = 31 * result + getChronology().hashCode();
	return result;
    }

    @Override
    public String toString() {
	DateTimeFormatter printer = ISODateTimeFormat.dateHourMinuteSecondFraction();
	printer = printer.withChronology(getChronology());
	StringBuffer buf = new StringBuffer(48);
	printer.printTo(buf, startInstant);
	buf.append('/');
	printer.printTo(buf, endInstant);
	return buf.toString();
    }
}
