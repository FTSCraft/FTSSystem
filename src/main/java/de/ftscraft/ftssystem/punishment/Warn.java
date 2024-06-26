/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.punishment;

import java.util.UUID;

public class Warn extends Punishment {

    public Warn(String reason, UUID author, long time, UUID player, String moreInfo, int id, boolean active) {
        super(reason, author, time, player, moreInfo, id, active);
    }

    @Override
    public PunishmentType getType() {
        return PunishmentType.WARN;
    }

}
