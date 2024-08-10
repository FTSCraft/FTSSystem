package de.ftscraft.ftssystem.main;

import com.earth2me.essentials.Essentials;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftssystem.channel.chatmanager.ChatManager;
import de.ftscraft.ftssystem.channel.chatmanager.ReichChatManager;
import de.ftscraft.ftssystem.commands.*;
import de.ftscraft.ftssystem.configs.ConfigManager;
import de.ftscraft.ftssystem.configs.ConfigVal;
import de.ftscraft.ftssystem.database.entities.DatabaseManager;
import de.ftscraft.ftssystem.listeners.*;
import de.ftscraft.ftssystem.menus.fts.MenuItems;
import de.ftscraft.ftssystem.poll.Umfrage;
import de.ftscraft.ftssystem.punishment.PunishmentManager;
import de.ftscraft.ftssystem.scoreboard.FTSScoreboardManager;
import de.ftscraft.ftssystem.utils.FileManager;
import de.ftscraft.ftssystem.utils.ForumHook.ForumHook;
import de.ftscraft.ftssystem.utils.PremiumManager;
import de.ftscraft.ftssystem.utils.Runner;
import de.ftscraft.ftssystem.utils.discordhook.DiscordHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

public class FtsSystem extends JavaPlugin {

    private HashMap<String, User> user;
    private Umfrage umfrage = null;

    public static final String PREFIX = "§7[§cFTS-System§7] ";
    private Economy econ;
    private Essentials essentialsPlugin;
    private ConfigManager configManager;
    private FileManager fileManager;
    private FTSScoreboardManager scoreboardManager;
    private MenuItems menuItems;
    private Engine engine;
    private PunishmentManager punishmentManager;
    private boolean wartung;
    private ChatManager chatManager;
    private PremiumManager premiumManager;
    private ForumHook forumHook;
    private DiscordHook discordHook;
    private static Logger pluginLogger;
    private static Logger chatLogger;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        pluginLogger = getLogger();
        chatLogger = Logger.getLogger("Chat");
        pluginLogger.info("Starting Plugin...");
        hook();

        init();
        postInit();
        pluginLogger.info("Plugin started!");
    }


    private void hook() {
        setupEconomy();
        engine = (Engine) getServer().getPluginManager().getPlugin("FTSEngine");
        if (getServer().getPluginManager().isPluginEnabled("Essentials"))
            essentialsPlugin = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
    }

    @Override
    public void onDisable() {
        save();
    }

    private void save() {
        configManager.setConfig(ConfigVal.WARTUNG, isInWartung());
        configManager.setConfig(ConfigVal.MESSAGES, configManager.getAutoMessages());

        configManager.save();
        fileManager.savePremium();
    }

    private void init() {
        user = new HashMap<>();
        configManager = new ConfigManager(this);
        fileManager = new FileManager(this);
        chatManager = new ReichChatManager(this);
        punishmentManager = new PunishmentManager(this);
        scoreboardManager = new FTSScoreboardManager(this);
        menuItems = new MenuItems();
        premiumManager = new PremiumManager(this);
        forumHook = new ForumHook(this);
        //TODO
        discordHook = new DiscordHook("oma");
        new CMDakte(this);
        new CMDbroadcast(this);
        new CMDcheckcv(this);
        new CMDdurchsage(this);
        new CMDfts(this);
        new CMDftssystem(this);
        new CMDpasswort(this);
        new CMDpremium(this);
        new CMDpu(this);
        new CMDradius(this);
        new CMDsetvotehome(this);
        new CMDtravel(this);
        new CMDtutorialbuch(this);
        new CMDumfrage(this);
        new CMDvoteban(this);
        new CMDvotehome(this);
        new CMDwartung(this);
        new CMDrepair(this);
        new CMDxpstore(this);

        new PortalListener(this);
        new DeathListener(this);
        new CommandListener(this);
        new ChatListener(this);
        new JoinListener(this);
        new EssentialsListener(this);
        new QuitListener(this);
        new LoginListener(this);
        new InvClickListener(this);
        new SneakListener(this);
        new EntityDeathListener(this);
        new PlayerAttackListener(this);
        new PlayerOpenSignListener(this);
        new PlayerInteractEntityListener(this);
        new LootGenerateListener(this);
        new FishingListener(this);
        new GrindstoneListener(this);
        new EnchantListener(this);
        new PlayerInteractListener(this);
        new FurnaceBurnListener(this);

        new Runner(this);

        Iterator<Recipe> recipes = Bukkit.recipeIterator();
        while (recipes.hasNext()) {
            Recipe recipe = recipes.next();

            if (recipe instanceof FurnaceRecipe furnaceRecipe) {

                if (furnaceRecipe.getInput().getType() == Material.RAW_GOLD || furnaceRecipe.getInput().getType() == Material.GOLD_ORE) {
                    recipes.remove();
                }

            } else if (recipe instanceof BlastingRecipe blastingRecipe) {

                if (blastingRecipe.getInput().getType() == Material.RAW_GOLD || blastingRecipe.getInput().getType() == Material.GOLD_ORE) {
                    recipes.remove();
                }

            }

        }

    }

    private void postInit() {
        wartung = configManager.isWartung();

        fileManager.loadSecrets();
        fileManager.loadPremium();
        premiumManager.checkPremiumPlayers();
        try {
            databaseManager = new DatabaseManager(getDataFolder().getAbsolutePath() + "/ftssystem.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public User getUser(Player player) {
        return user.get(player.getName());
    }

    public HashMap<String, User> getUser() {
        return user;
    }

    public Economy getEcon() {
        return econ;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Umfrage getUmfrage() {
        return umfrage;
    }

    public void setUmfrage(Umfrage umfrage) {
        this.umfrage = umfrage;
    }

    public FTSScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    public PremiumManager getPremiumManager() {
        return premiumManager;
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public Engine getEngine() {
        return engine;
    }

    public MenuItems getMenuItems() {
        return menuItems;
    }

    public boolean isInWartung() {
        return wartung;
    }

    public void setWartung(boolean wartung) {
        this.wartung = wartung;
    }

    public ForumHook getForumHook() {
        return forumHook;
    }

    public static Logger getPluginLogger() {
        return pluginLogger;
    }

    public static Logger getChatLogger() {
        return chatLogger;
    }

    public Essentials getEssentialsPlugin() {
        return essentialsPlugin;
    }

    public DiscordHook getDiscordHook() {
        return discordHook;
    }
}
