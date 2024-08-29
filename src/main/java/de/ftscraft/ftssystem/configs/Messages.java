package de.ftscraft.ftssystem.configs;

import net.kyori.adventure.text.format.NamedTextColor;

public class Messages {

    public static final NamedTextColor HIGHLIGHT_COLOR = NamedTextColor.RED;

    public static final String
            PREFIX = "§7[§4FTS-System§7] ",
            MINI_PREFIX = "<gray>[<dark_red>FTS-System</dark_red>] ",
            NO_PERM = PREFIX + "Dazu hast du keine Rechte!",
            NUMBER = PREFIX + "Bitte benutz eine gültige Zahl",
            NO_ACTIVE_CHANNEL = PREFIX + "Du hast keinen aktiven Channel ausgewählt. Mache dies mit /channel aktiv <Name>",
            NOW_ACTIVE_CHANNEL = PREFIX + "%s ist nun dein aktiver Channel!",
            CHOOSE_CHANNEL = PREFIX + "Bitte wähle einen Channel. Du kannst die Channel mit §c/channel list §7sehen!",
            UMFRAGE_CREATED = PREFIX + "Die Umfrage mit der Frage §c%s§7wurde erstellt!",
            NO_UMFRAGE_FOUND = PREFIX + "Du hast keine Umfrage mit /umfrage create FRAGE erstellt!",
            UMFRAGE_ALREADY_STARTED = PREFIX + "Die Umfrage wurde schon gestartet!",
            UMFRAGE_ADDED_OPTION = PREFIX + "Die Option §c%s§7wurde hinzugefügt!",
            UMFRAGE_NOT_STARTED = PREFIX + "Die Umfrage wurde noch gar nicht gestartet",
            UMFRAGE_STATED = PREFIX + "Die Umfrage wurde gestartet!",
            UMFRAGE_ALREADY_CONFIG = PREFIX + "Es wird derzeit eine Umfrage erstellt / ist am laufen",
            PLAYER_NOT_FOUND = PREFIX + "Der Spieler wurde nicht gefunden!",
            WRONG_USAGE = PREFIX + "Bitte nutze den Befehl so: " + HIGHLIGHT_COLOR + "%s",
            NOT_ENOUGH_MONEY = PREFIX + "Du brauchst %d Taler.",
            ONLY_PLAYER = PREFIX + "Dieser CMD ist nur für Spieler!";


    public static final String

    HELP_FTSSYSTEM =
            """
                    §c---- §e/ftssystem §c----
                    §e/ftssystem fake <Join|Leave> §b- Täusche alle Spieler mit diesem einfachen Trick!
                    §e/ftssystem playtime §b- Zeigt Spieler ohne Bürgerrang und über 50h Spielzeit an.""";

}
