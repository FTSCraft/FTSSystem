/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.punishment;

public enum TimeUnits {

    SECOND(1000, "s"),
    MINUTE(1000 * 60, "m"),
    HOUR(1000 * 60 * 60, "h"),
    DAY(1000 * 60 * 60 * 24, "d"),
    WEEK(1000 * 60 * 60 * 24 * 7, "w");

    private final int millis;
    private final String unit;

    TimeUnits(int millis, String unit) {
        this.millis = millis;
        this.unit = unit;
    }

    public int getMillis() {
        return millis;
    }

    public String getUnit() {
        return unit;
    }

    public static TimeUnits getTimeUnitByUnit(String unit) {
        for (TimeUnits a : TimeUnits.values()) {
            if (a.getUnit().equalsIgnoreCase(unit))
                return a;
        }
        return null;
    }

}
