/*
 * Copyright (c) 2019.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.utils;

import de.ftscraft.ftssystem.main.FtsSystem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileManager {

    private FtsSystem plugin;

    private File book_file;
    private FileConfiguration book_cfg;

    private File premiumFile;
    private FileConfiguration premiumCfg;

    private File secretsFile;
    private FileConfiguration secretsConfig;

    String bookCMD;
    String bookBlockreichCMD;

    public FileManager(FtsSystem plugin) {
        this.plugin = plugin;

        this.book_file = new File(plugin.getDataFolder() + "//tutorialbook.yml");
        this.book_cfg = YamlConfiguration.loadConfiguration(book_file);

        this.premiumFile = new File(plugin.getDataFolder() + "//premium.yml");
        this.premiumCfg = YamlConfiguration.loadConfiguration(premiumFile);

        this.secretsFile = new File(plugin.getDataFolder() + "//secrets.yml");
        this.secretsConfig = YamlConfiguration.loadConfiguration(secretsFile);

        loadBookComamnd();
    }

    public void loadPremium() {
        for (String user : premiumCfg.getKeys(false)) {
            long seconds = premiumCfg.getLong(user+".time");
            plugin.getPremiumManager().addPremiumPlayer(UUID.fromString(user), seconds);
        }
    }

    public void savePremium() {
        PremiumManager premiumManager = plugin.getPremiumManager();
        for (String key : premiumCfg.getKeys(false)) {
            premiumCfg.set(key, null);
        }
        premiumManager.getPremiumPlayers().forEach((uuid, aLong) -> {
            premiumCfg.set(uuid.toString()+".time", aLong);
        });

        try {
            premiumCfg.save(premiumFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadSecrets() {
        plugin.getForumHook().setApiKey(secretsConfig.getString("apiKey"));
        plugin.getForumHook().setApiUser(secretsConfig.getString("apiUser"));
        plugin.getForumHook().setPremiumGroupId(secretsConfig.getInt("premiumGroupId"));
        APIHandling.setAntiVpnApiKey(secretsConfig.getString("antiVpnApiKey"));
    }

    public void loadBookComamnd() {

        book_cfg.options().copyDefaults(true);
        book_cfg.addDefault("command", "give <player> written_book 1 0 {pages:['[\"\",{\"text\":\"Inhaltsverzeichnis\\\\n\\\\n1. \"},{\"text\":\"Erste Schritte\",\"clickEvent\":{\"action\":\"change_page\",\"value\":2}},{\"text\":\"\\\\n2. \"},{\"text\":\"Geld verdienen\",\"clickEvent\":{\"action\":\"change_page\",\"value\":10}},{\"text\":\"\\\\n3. \"},{\"text\":\"Wichtige Plugins\",\"clickEvent\":{\"action\":\"change_page\",\"value\":12}},{\"text\":\"\\\\n4. \"},{\"text\":\"Orientierung\",\"clickEvent\":{\"action\":\"change_page\",\"value\":13}},{\"text\":\"\\\\n5. \"},{\"text\":\"Roleplay\",\"clickEvent\":{\"action\":\"change_page\",\"value\":14}},{\"text\":\"\\\\n6. \"},{\"text\":\"Rassen\",\"clickEvent\":{\"action\":\"change_page\",\"value\":20}},{\"text\":\"\\\\n7. \"},{\"text\":\"Ich brauche Hilfe\",\"clickEvent\":{\"action\":\"change_page\",\"value\":24}},{\"text\":\"\\\\n8. \"},{\"text\":\"Links und Videos\",\"clickEvent\":{\"action\":\"change_page\",\"value\":25}}]','[\"\",{\"text\":\"Erste Schritte\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nIhr befindet euch in einer Höhle in Taufeld.\\\\nDort solltet ihr alle Anweisungen beachten.\\\\n\\\\nAm Ende müsst ihr im \",\"color\":\"reset\"},{\"text\":\"Regelwerk\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://ftscraft.de/regelwerk/\"}},{\"text\":\" ein Passwort suchen und mit \",\"color\":\"reset\"},{\"text\":\"/passwort Wort\",\"color\":\"red\"},{\"text\":\" bestätigen.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Am Anfang ist es euch möglich entweder in der Wildnis zu bauen oder euch einer Stadt anzuschließen. Wir \"},{\"text\":\"empfehlen\",\"color\":\"red\"},{\"text\":\", dass ihr euch eine Stadt sucht.\\\\n\\\\nEine Auflistung aller Städte findet ihr \",\"color\":\"reset\"},{\"text\":\"hier\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.ftscraft.de/t/die-staedte-parsifals/2618\"}},{\"text\":\".\",\"underlined\":true,\"color\":\"red\"}]','{\"text\":\"Achtet darauf, dass euer Charakter sich der Lore und der Kultur der Stadt anpasst. Die Städte achten sehr darauf, einen roten Faden in ihrer Stadt zu wahren.\"}','[\"\",{\"text\":\"Solange ihr ein \"},{\"text\":\"Reisender \",\"color\":\"gold\"},{\"text\":\"seid, genießt ihr eine Findungsphase. Erst ab dem \",\"color\":\"reset\"},{\"text\":\"Bürger \",\"color\":\"gold\"},{\"text\":\"wird Roleplay zur Pflicht!\\\\n\\\\nEin \",\"color\":\"reset\"},{\"text\":\"/ausweis\",\"color\":\"red\"},{\"text\":\" und eine Charaktervorstellung ist also für den Anfang noch keine Pflicht!\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Zum \"},{\"text\":\"Bürger\",\"color\":\"gold\"},{\"text\":\" steigt ihr auf, wenn ihr eine \",\"color\":\"reset\"},{\"text\":\"Charaktervorstellung\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.ftscraft.de/c/charaktervorstellungen/45\"}},{\"text\":\" angelegt habt, ihr den Ausweis an diese anpasst und ihr für eure Rasse einen geeigneten Skin ausgesucht habt.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Solltet ihr keine Idee haben welche Rasse oder Stadt zu euch passen könnte, fragt jederzeit einen \"},{\"text\":\"Teamler \",\"color\":\"aqua\"},{\"text\":\"oder gerne auch normale Spieler fragen.\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Redet\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.com/invite/E5C2pU8\"}},{\"text\":\" mit ihnen und zögert nicht euch beraten zu lassen!\\\\n \",\"color\":\"reset\"}]','{\"text\":\"Alle Mitglieder der Community sind daran interessiert, gutes Roleplay zu betreiben. \\\\n\\\\nAus diesem Grund werden sie euch neutral beraten, damit ihr das Beste aus eurem Charakter und euren Ideen herausholen könnt!\"}','{\"text\":\"In Taufeld findet ihr zudem NPC\\'s, die euch ebenfalls einige wichtige Hilfestellungen zu den Plugins, unserer Lore und unserem Roleplay erklären.\\\\n\\\\nSprecht optional mit diesen, es wird euch nicht schaden!\\\\n \"}','[\"\",{\"text\":\"Geld verdienen\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nDie schnellste Methode ist das Voten. Das kannst du jeden Tag mit\",\"color\":\"reset\"},{\"text\":\" /vote. \",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nAnsonsten müsst ihr Handel treiben oder in der Abbauwelt nach Gold und Kupfer suchen.\",\"color\":\"reset\"}]','{\"text\":\"Kupferbarren und Rohgold könnt ihr dann in einer Bank zu Taler umtauschen.\\\\n\\\\nAuch Events von Spielern oder dem Server sind ein guter Anlass, um etwas Geld zu verdienen.\\\\n \"}','[\"\",{\"text\":\"Wichtige Plugins\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Krankheiten\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.ftscraft.de/t/disease-krankheiten-ihre-tuecken-und-entsprechende-heilmittel/218\"}},{\"text\":\", eine ausgewogene \",\"color\":\"reset\"},{\"text\":\"Ernährung\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.ftscraft.de/t/ftssurvival-unser-eigenes-ernaehrungssystem/215\"}},{\"text\":\",\",\"color\":\"reset\"},{\"text\":\" das veränderte \"},{\"text\":\"Wachstum von Pflanzen\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.ftscraft.de/t/pwnplantgrowth-das-wachstum-der-pflanzen/137/33\"}},{\"text\":\" und das \",\"color\":\"reset\"},{\"text\":\"Skillsystem\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.ftscraft.de/t/ftsskills-unser-eigenes-skillsystem/5364\"}},{\"text\":\" sind nur einige Schwierigkeiten.\\\\n\\\\nWeitere Plugins bei uns im \",\"color\":\"reset\"},{\"text\":\"Forum\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.ftscraft.de/c/ftscraft-allgemein/plugins/31\"}},{\"text\":\"!\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Orientierung\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nWenn ihr eine Übersicht der Weltkarte möchtet, könnt ihr sie \",\"color\":\"reset\"},{\"text\":\"hier\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://map.ftscraft.de/\"}},{\"text\":\" aufrufen.\\\\n\\\\nHier findet ihr Standorte von Städten oder schöne Plätze für euren Unterschlupf.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Roleplay\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nWir sind ein RPG Server, weshalb ihr die Möglichkeit habt, mit einem von euch ausgedachten Charakter, in die mittelalterliche Welt des fiktiven Landes Parsifal einzutauchen.\\\\n\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Denkt euch einen Charakter aus und baut euch im mittelalterlichen Parsifal euer Leben auf.\\\\n\\\\nRollenspiel betreibt ihr im \"},{\"text\":\"/local\",\"color\":\"red\"},{\"text\":\" Channel und oder mithilfe von \",\"color\":\"reset\"},{\"text\":\"/rp\",\"color\":\"red\"},{\"text\":\".\",\"color\":\"reset\"}]','{\"text\":\"Im RP spielt ihr als einen ausgedachten Charakter. Auf Events oder in einer Stadt könnt ihr andere Charaktere kennenlernen und mit ihnen Kontakt knüpfen.\\\\n\\\\n\\\\n \"}','[\"\",{\"text\":\"Wenn ihr einen Ausweis erstellt habt, ist es euch möglich mit \"},{\"text\":\"*\",\"color\":\"red\"},{\"text\":\" vor eurer Nachricht rplich zu schreiben.\\\\n\\\\nOOC-Wörter solltet ihr mit \",\"color\":\"reset\"},{\"text\":\"((Wort)) \",\"color\":\"red\"},{\"text\":\"ausklammern.\\\\n\\\\nNutzt diese Klammern wenig!\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Im globalen Chat greift ihr bitte ausschließlich auf Begrüßungen und Support zurück.\\\\n\\\\nMit \"},{\"text\":\"/channel\",\"color\":\"red\"},{\"text\":\" seht ihr alle wichtigen Befehle und Channel-Funktionen.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Um eine Nachricht an einen Spieler zu schreiben, verwendet \"},{\"text\":\"/taube\",\"color\":\"red\"},{\"text\":\" Spielername\",\"color\":\"red\"},{\"text\":\" oder fürs Erste \",\"color\":\"reset\"},{\"text\":\"/w Spielername\",\"color\":\"red\"},{\"text\":\".\\\\n\\\\nAuch \",\"color\":\"reset\"},{\"text\":\"/letter Nachricht\",\"color\":\"red\"},{\"text\":\" und \",\"color\":\"reset\"},{\"text\":\"/post Spielername\",\"color\":\"red\"},{\"text\":\" eignen sich hervorragend dafür.\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Rassen\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nIn Parsifal könnt ihr \",\"color\":\"reset\"},{\"text\":\"Mensch\",\"color\":\"red\"},{\"text\":\", \",\"color\":\"reset\"},{\"text\":\"Elf\",\"color\":\"red\"},{\"text\":\", \",\"color\":\"reset\"},{\"text\":\"Zwerg \",\"color\":\"red\"},{\"text\":\"oder auch \",\"color\":\"reset\"},{\"text\":\"Ork \",\"color\":\"red\"},{\"text\":\"sein.\\\\n\\\\nIhr könnt euch einer dieser \",\"color\":\"reset\"},{\"text\":\"Rassen\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.ftscraft.de/t/die-unterrassen-von-parsifal/3018\"}},{\"text\":\" bzw. Unterrassen und Kulturen anschließen.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Alle Rassen haben verschiedene Vor- und Nachteile, die sich in unserem \"},{\"text\":\"Kampfsystem\",\"color\":\"red\"},{\"text\":\" äußern.\\\\n\\\\nMeistens werden Kämpfe nämlich nicht im Minecraft-PVP, sondern in rplichen Kämpfen mit Würfeln ausgetragen.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Mit \"},{\"text\":\"/f show Stadtname\",\"color\":\"red\"},{\"text\":\" könnt ihr sehen, welcher Spieler aus der entsprechenden Stadt gerade online ist.\\\\n\\\\nWendet euch am besten direkt an diese Person, wenn ihr euch für eine Stadt entschieden habt!\",\"color\":\"reset\"}]','{\"text\":\"Tretet einer Stadt bei, die euch selbst gefällt und die zu euren Vorstellungen passt!\\\\n\\\\nTretet nicht einfach der ersten Stadt bei, die euch anschreibt oder mit euch Roleplay betreibt!\"}','[\"\",{\"text\":\"Wo bekomme ich Hilfe?\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nMit \",\"color\":\"reset\"},{\"text\":\"/ticket\",\"color\":\"red\"},{\"text\":\" könnt ihr ein Ticket erstellen. Ansonsten fragt einfach im \",\"color\":\"reset\"},{\"text\":\"globalen Chat\",\"color\":\"red\"},{\"text\":\" mit \",\"color\":\"reset\"},{\"text\":\"!\",\"color\":\"red\"},{\"text\":\" vor eurer Nachricht nach Hilfe.\\\\n\\\\nGerne könnt ihr auch jederzeit einen \",\"color\":\"reset\"},{\"text\":\"Teamler \",\"color\":\"aqua\"},{\"text\":\"fragen.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Wichtige Links \",\"color\":\"red\"},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Erste-Schritte-Video\",\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.youtube.com/watch?v=D_dCQrqOFwo&t\"}},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Links für Neulinge\",\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.ftscraft.de/t/die-wichtigsten-links-und-hinweise-fuer-neulinge/10737\"}},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Einsteigerguide\",\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.ftscraft.de/t/der-einsteigerguide-fuer-unseren-server/1206\"}},{\"text\":\"\\\\n\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Solange ihr ein \"},{\"text\":\"Reisender \",\"color\":\"gold\"},{\"text\":\"seid, könnt ihr dir dieses Buch mit \",\"color\":\"reset\"},{\"text\":\"/tutorialbook\",\"color\":\"red\"},{\"text\":\" holen.\\\\n\\\\nFragen auch jederzeit auf \",\"color\":\"reset\"},{\"text\":\"Discord \",\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/ZMcQEw8jbW\"}},{\"text\":\"oder im \",\"color\":\"reset\"},{\"text\":\"Forum\",\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.ftscraft.de/\"}},{\"text\":\"!\",\"color\":\"reset\"}]'],title:Tutorialbuch,author:FTSCraft,display:{Lore:[\"Dieses Buch wird euch den Anfang leichter machen\"]}}");
        book_cfg.addDefault("command_blockreich", "give <player> written_book 1 0 {pages:['[\"\",{\"text\":\"Inhaltsverzeichnis\\\\n\\\\n1. \"},{\"text\":\"Erste Schritte\",\"color\":\"red\",\"clickEvent\":{\"action\":\"change_page\",\"value\":2}},{\"text\":\"\\\\n2. \",\"color\":\"reset\"},{\"text\":\"Anarchie\",\"color\":\"red\",\"clickEvent\":{\"action\":\"change_page\",\"value\":7}},{\"text\":\"\\\\n3. \",\"color\":\"reset\"},{\"text\":\"Roleplay\",\"color\":\"red\",\"clickEvent\":{\"action\":\"change_page\",\"value\":9}},{\"text\":\"\\\\n4. \",\"color\":\"reset\"},{\"text\":\"Citybuild\",\"color\":\"red\",\"clickEvent\":{\"action\":\"change_page\",\"value\":11}},{\"text\":\"\\\\n5. \",\"color\":\"reset\"},{\"text\":\"Geld verdienen\",\"color\":\"red\",\"clickEvent\":{\"action\":\"change_page\",\"value\":12}},{\"text\":\"\\\\n6. \",\"color\":\"reset\"},{\"text\":\"PVP\",\"color\":\"red\",\"clickEvent\":{\"action\":\"change_page\",\"value\":14}},{\"text\":\"\\\\n7. \",\"color\":\"reset\"},{\"text\":\"Chats\",\"color\":\"red\",\"clickEvent\":{\"action\":\"change_page\",\"value\":16}},{\"text\":\"\\\\n8. \",\"color\":\"reset\"},{\"text\":\"Du brauchst Hilfe?\",\"color\":\"red\",\"clickEvent\":{\"action\":\"change_page\",\"value\":17}},{\"text\":\"\\\\n9. \",\"color\":\"reset\"},{\"text\":\"Nützliche Links\",\"color\":\"red\",\"clickEvent\":{\"action\":\"change_page\",\"value\":18}}]','[\"\",{\"text\":\"Erste Schritte\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nDu befindest dich in einer Höhle am Spawn und wirst dort in den Server eingewiesen. Am Ende musst du das Passwort im \",\"color\":\"reset\"},{\"text\":\"Regelwerk\",\"underlined\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://blockreich.me/regeln/\"}},{\"text\":\" finden. Dabei handelt es sich um ein Wort was nicht in den Satz passt.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Wenn ihr das Wort gefunden habt, gebt es mit \"},{\"text\":\"/passwort wort\",\"color\":\"red\"},{\"text\":\" ein.\\\\n\\\\nIm Anschluss gelangst du in das Spawn-Dorf.\\\\n\\\\nMit dem Befehl \",\"color\":\"reset\"},{\"text\":\"/karte\",\"color\":\"red\"},{\"text\":\" kannst du dir den Link zur Dynmap anzeigen lassen.\\\\n\\\\n \",\"color\":\"reset\"}]','{\"text\":\"Im Spawn-Dorf findest du NPC-Händler und eine Kutsche in die Farmwelt. In der Farmwelt kannst du dir Material beschaffen und nach Gold suchen - Gold ist bei uns die Währung.\"}','{\"text\":\"In der Farmwelt ist PVP jederzeit aktiviert. Solange du jedoch ein Reisender bist, genießt du einen Noob-Schutz. Du kannst also ganz in Ruhe in der Farmwelt deine Materialien und Geld beschaffen.\\\\n\\\\n \"}','[\"\",{\"text\":\"Setze dir unbedingt ein Home-Punkt mit \"},{\"text\":\"/sethome\",\"color\":\"red\"},{\"text\":\" an der Stelle, wo du dich aufbauen willst! Ansonsten kannst du mit \",\"color\":\"reset\"},{\"text\":\"/warp spawn\",\"color\":\"red\"},{\"text\":\" zum Spawn-Dorf gelangen und mit \",\"color\":\"reset\"},{\"text\":\"/home\",\"color\":\"red\"},{\"text\":\" zu deinem \\\\u0020Home-punkt.\\\\nMit \\\\u0020\",\"color\":\"reset\"},{\"text\":\"/t spawn\",\"color\":\"red\"},{\"text\":\" kommst du zu deiner Stadt.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Anarchie\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nWir haben auf unserem Server sehr wenig Regeln, was dafür sorgt, dass Spieler ihre Angelegenheiten selbst lösen sollen. Wenn ihr Hilfe braucht, sucht sie euch. Das Team greift nur in äußersten Notfällen ein.\",\"color\":\"reset\"}]','{\"text\":\"Nur weil Anarchie ein großer Punkt bei uns auf dem Server ist heißt das nicht, dass alles erlaubt ist und alles geduldet wird!\\\\n\\\\nToxisches Verhalten und \\\\\"steht doch so im Regelwerk um jeden Preis\\\\\" wird nicht geduldet.\"}','[\"\",{\"text\":\"Roleplay\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nDas Rollenspiel kann bei uns jeder so gestalten wie er möchte. Jeder kann die Rasse, den Charakter und die Religion spielen, die er möchte. Vorher bedarf das jedoch einer Vorstellung im Forum!\",\"color\":\"reset\"}]','{\"text\":\"Das Rollenspiel ist keine Pflicht! Trotzdem gibt es viele Spieler, die untereinander Rollenspiel betreiben und dadurch in Kontakt treten und miteinander interagieren.\\\\n\\\\nVersucht euch dem RP einzubringen, das fördert euren Spielspaß enorm!\"}','[\"\",{\"text\":\"Citybuild\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nJeder Spieler kann direkt eine Stadt gründen und gegen wenig Geld erweitern. Pro Chunk kostet die Stadt jedoch regelmäßig abgaben. Mit dem Aufstieg der Stadt, bekommst du auch immer neue Ränge.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Geld verdienen\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nDas Voten ist die wohl einfachste Methode um etwas Geld zu verdienen. Deine Vote-Möglichkeiten findest du mit \",\"color\":\"reset\"},{\"text\":\"/vote\",\"color\":\"red\"},{\"text\":\" heraus.\\\\n\\\\nAuch die NPC-Händler vom Spawn-Dorf bieten eine gute Möglichkeit.\",\"color\":\"reset\"}]','{\"text\":\"Ansonsten bleibt dir immer die Möglichkeit mit anderen Spielern handel zu betreiben.\\\\n\\\\nBaue dir dafür Handelsbeziehungen auf und trete mit anderen Spielern und oder Städten in Kontakt.\\\\n \"}','[\"\",{\"text\":\"PVP\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nAuch PVP ist natürlich ein großer Aspekt von Blockreich. PVP ist in der Wildnis der normalen Welt nur erlaubt, wenn eine Tötungsabsicht vorher klar angekündigt wird. \",\"color\":\"reset\"}]','{\"text\":\"Ständiges töten, belästigen und nerven von Spielern durch seine PVP-Komplexe wird nicht geduldet.\\\\n\\\\nWenn ihr PVP betreiben wollt und Spieler ohne Sinn töten wollt, ist Blockreich nicht der richtige Ort für euch!\"}','[\"\",{\"text\":\"Chats\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nAlle Befehle zu Chats findest du unter \",\"color\":\"reset\"},{\"text\":\"/channel\",\"color\":\"red\"},{\"text\":\". Der globale Chat ist für ooclichen Talk gedacht. Der lokale Chat reicht 30 Blöcke, der Flüstern 5 Blöcke, der Schreien 100 Blöcke.\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Wo bekomme ich hilfe?\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\nMit \",\"color\":\"reset\"},{\"text\":\"/ticket\",\"color\":\"red\"},{\"text\":\" kannst du ein Ticket erstellen. Ansonsten frage einfach im \",\"color\":\"reset\"},{\"text\":\"globalen Chat\",\"color\":\"red\"},{\"text\":\".\\\\n\\\\nGerne kannst du dich auch jederzeit an einen \",\"color\":\"reset\"},{\"text\":\"Teamler \",\"color\":\"aqua\"},{\"text\":\"wenden.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Nützliches\",\"color\":\"red\"},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Homepage\",\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://blockreich.me/\"}},{\"text\":\"\\\\n\",\"color\":\"reset\"},{\"text\":\"Discord \",\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/ZvwDUBNHJd\"}},{\"text\":\"\\\\n\",\"color\":\"red\"},{\"text\":\"Forum\",\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://forum.blockreich.me/\"}},{\"text\":\"\\\\n\\\\nSolange du ein \",\"color\":\"reset\"},{\"text\":\"Reisender \",\"color\":\"gold\"},{\"text\":\"bist, kannst du dir dieses Buch mit \",\"color\":\"reset\"},{\"text\":\"/tutorialbook\",\"color\":\"red\"},{\"text\":\" holen.\",\"color\":\"reset\"}]'],title:Buch,author:\"http://minecraft.tools/\"}");
        try {
            book_cfg.save(book_file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.bookCMD = book_cfg.getString("command");
        this.bookBlockreichCMD = book_cfg.getString("command_blockreich");

    }

    public String getBookCMD() {
        return bookCMD;
    }

    public String getBookBlockreichCMD() {
        return bookBlockreichCMD;
    }
}
