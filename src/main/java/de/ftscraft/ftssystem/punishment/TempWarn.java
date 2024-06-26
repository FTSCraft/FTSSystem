/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.punishment;

import java.util.UUID;

public class TempWarn extends TemporaryPunishment {

    public TempWarn(String reason, UUID author, long time, long until, UUID player, String moreInfo, int id, boolean active) {
        super(reason, author, time, until, player, moreInfo, id, active);
    }

    @Override
    public PunishmentType getType() {
        return PunishmentType.TEMP_WARN;
    }

}
