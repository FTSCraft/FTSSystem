/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.punishment;

import de.ftscraft.ftssystem.utils.Utils;

import java.util.Calendar;
import java.util.UUID;

public class TemporaryPunishment extends Punishment {

    private long until;

    public TemporaryPunishment(PunishmentType type, String reason, UUID author, long time, long until, UUID player, String moreInfo, int id, boolean active) {
        super(type, reason, author, time, player, moreInfo, id, active);
        this.until = until;
    }

    public long untilInMillis() {
        return until;
    }

    public String untilAsString() {
        return Utils.convertToTime(until);
    }

    public String untilAsCalString() {

        Calendar cal = new Calendar.Builder().setInstant(until).build();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        return (day < 10 ? "0"+day : day) + "." + (month < 10 ? "0"+month : month) + "." + year + " - " + (hour < 10 ? "0"+hour : hour) + ":" + (min < 10 ? "0"+min : min);

    }

    public long getUntil() {
        return until;
    }

    public void setUntil(long until) {
        this.until = until;
    }
}
