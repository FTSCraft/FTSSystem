package de.ftscraft.ftssystem.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.ftscraft.ftssystem.punishment.Punishment;
import de.ftscraft.ftssystem.punishment.PunishmentType;
import de.ftscraft.ftssystem.punishment.TemporaryPunishment;

import java.util.UUID;

@SuppressWarnings("unused")
@DatabaseTable(tableName = "punishments")
public class PunishmentEntity {

    @DatabaseField(generatedId = true)
    int ID;
    @DatabaseField(canBeNull = false)
    String reason;
    @DatabaseField(canBeNull = false)
    //UUID
    String author;
    @DatabaseField(canBeNull = false)
    long time;
    @DatabaseField(canBeNull = false)
    //UUID
    String player;
    @DatabaseField(canBeNull = false, defaultValue = "-")
    String moreInfo;
    @DatabaseField(canBeNull = false)
    boolean active;
    @DatabaseField(canBeNull = false, defaultValue = "-1")
    long until;
    @DatabaseField(canBeNull = false)
    String type;

    public PunishmentEntity() {

    }

    public PunishmentEntity(String reason, String author, long time, String player, String moreInfo, boolean active, long until, String type) {
        this.reason = reason;
        this.author = author;
        this.time = time;
        this.player = player;
        this.moreInfo = moreInfo;
        this.active = active;
        this.until = until;
        this.type = type;
    }

    public PunishmentEntity(String reason, UUID author, long time, String player, String moreInfo, boolean active, long until, String type) {
        this.reason = reason;
        this.author = author.toString();
        this.time = time;
        this.player = player;
        this.moreInfo = moreInfo;
        this.active = active;
        this.until = until;
        this.type = type;
    }

    public Punishment turnIntoPunishment() {
        Punishment punishment;
        PunishmentType punishmentType = PunishmentType.valueOf(type);
        if (!type.contains("TEMP"))
            punishment = new Punishment(punishmentType, reason, UUID.fromString(author), time, UUID.fromString(player), moreInfo, ID, active);
        else punishment = new TemporaryPunishment(punishmentType, reason, UUID.fromString(author), time, until, UUID.fromString(player), moreInfo, ID, active);
        return punishment;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getUntil() {
        return until;
    }

    public void setUntil(long until) {
        this.until = until;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
