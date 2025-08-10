/*
 * Copyright (c) 2021.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.configs;

public enum ConfigVal {

    WARTUNG("wartung"),
    RESOURCE_PACK_LINK("resourcepack.link"),
    RESOURCE_PACK_HASH("resourcepack.hash"),
    MESSAGES("messages");

    private final String path;

    ConfigVal(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
