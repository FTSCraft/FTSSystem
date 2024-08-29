/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.punishment;

import de.ftscraft.ftsutils.uuidfetcher.UUIDFetcher;

import java.util.Calendar;
import java.util.UUID;

public class Punishment {

    protected final String reason;
    protected final UUID author;
    protected final long time;
    protected final UUID player;
    protected String moreInfo;
    protected boolean active;
    protected final int ID;
    protected final PunishmentType type;

    public Punishment(PunishmentType type, String reason, UUID author, long time, UUID player, String moreInfo, int id, boolean active) {
        this.reason = reason;
        this.author = author;
        this.time = time;
        this.player = player;
        this.moreInfo = moreInfo;
        this.active = active;
        this.ID = id;
        this.type = type;
    }

    public UUID getPlayer() {
        return player;
    }

    public String getMoreInformation() {
        return moreInfo;
    }

    public String getReason() {
        return reason;
    }

    public UUID getAuthor() {
        return author;
    }

    public String getAuthorName() {
        return UUIDFetcher.getName(author);
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public long getTime() {
        return time;
    }

    public int getID() {
        return ID;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public PunishmentType getType() {
        return type;
    }

    public String createdOn() {

        Calendar cal = new Calendar.Builder().setInstant(time).build();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        return (day < 10 ? "0"+day : day) + "." + (month < 10 ? "0"+month : month) + "." + year + " - " + (hour < 10 ? "0"+hour : hour) + ":" + (min < 10 ? "0"+min : min);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
