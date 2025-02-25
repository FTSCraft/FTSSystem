/*
 * Copyright (c) 2020.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.scoreboard;

import org.bukkit.entity.Player;

import static de.ftscraft.ftsengine.utils.Ausweis.Gender;

public enum TeamPrefixs {

    ADMIN("§bAdmin", "§bAdmin", "ftsengine.admin", "000Admin"),
    DEV("§bDev", "§bDev", "ftsengine.dev", "001Dev"),
    MODERATOR("§bModerator", "§bModeratorin", "ftsengine.moderator", "002Moderator"),
    HELFER("§bHelfer", "§bHelferin", "ftsengine.helfer", "003Helfer"),
    PREMIUM("§cPremium", "§cPremium", "ftsengine.premium", "004Premium"),
    SCHREIBER("§cSchreiber", "§cSchreiberin", "ftsengine.schreiber", "0041Schreib"),
    ARCHITEKT("§eArchitekt", "§eArchitektin", "ftsengine.architekt", "005Architekt"),
    RAUBER("§9Räuber", "§9Räuberin", "ftsengine.rauber", "008Rauber"),
    MEJSTER("§9Medikus", "§9Medika", "ftsengine.mejster", "009Mejster"),
    PRIESTER("§9Priester", "§9Priesterin", "ftsengine.priester", "010Priester"),
    BRAUMEISTER("§9Braumeister", "§9Braumeisterin", "ftsengine.braumeister", "011BMeister"),
    HERZOG("§2Herzog", "§2Herzogin", "ftsengine.herzog", "014Herzog"),
    HEILIGER("§2Heiliger", "§2Heilige", "ftsengine.heiliger", "015Heiliger"),
    FLOTTENFUHRER("§2Flottenführer", "§2Flottenführerin", "ftsengine.flottenfuhrer", "015Flotte"),
    FURST("§2Fürst", "§2Fürstin", "ftsengine.furst", "016Furst"),
    GRAF("§2Graf", "§2Gräfin", "ftsengine.graf", "017Graf"),
    BURGHERR("§2Burgherr", "§2Burgherrin", "ftsengine.burgherr", "018Burgherr"),
    RITTER("§2Ritter", "§2Ritterin", "ftsengine.ritter", "019Ritter"),
    GILDENHERR("§2Gildenherr", "§2Gildenherrin", "ftsengine.gildenherr", "021Gildenherr"),
    STADTHERR("§2Stadtherr", "§2Stadtherrin", "ftsengine.stadtherr", "022Stadtherr"),
    MEISTER("§2Meister", "§2Meisterin", "ftsengine.meister", "023Meister"),
    BURGERMEISTER("§2Bürgermeister", "§2Bürgermeisterin", "ftsengine.burgermeister", "024BMeister"),
    SIEDLER("§2Siedler", "§2Siedlerin", "ftsengine.siedler", "025Siedler"),
    EINSIEDLER("§2Einsiedler", "§2Einsiedlerin", "ftsengine.einsiedler", "0261Einsiedler"),
    VOGT("§6Vogt", "§6Vögtin", "ftsengine.vogt", "026Vogt"),
    HEROLD("§6Herold", "§6Heroldin", "ftsengine.herold", "027Herold"),
    HANDLER("§6Händler", "§6Händlerin", "ftsengine.handler", "034Handler"),
    BURGER("§6Bürger", "§6Bürgerin", "ftsengine.burger", "035Burger"),
    REISENDER("§6Reisender", "§6Reisende", "ftsengine.reisender", "036Reisender"),
    NEULING("§6Neuling", "§6Neuling", "ftsengine.neuling", "037Neuling");

    final String mprefix;
    final String fprefix;
    final String permission;
    final String teamName;

    TeamPrefixs(String mprefix, String fprefix, String permission, String teamName) {
        this.teamName = teamName;
        this.mprefix = mprefix;
        this.fprefix = fprefix;
        this.permission = permission;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPermission() {
        return permission;
    }

    public String getPrefix(Gender gender) {
        if (gender == null || gender == Gender.MALE || gender == Gender.DIVERS) {
            return mprefix;
        } else if (gender == Gender.FEMALE) {
            return fprefix;
        }
        return null;
    }

    public static String getPrefix(Player p, Gender gender) {
        for (TeamPrefixs value : values()) {
            if (p.hasPermission(value.getPermission()))
                return value.getPrefix(gender);
        }
        return NEULING.getPrefix(Gender.MALE);
    }

}
