package club.flame.disqualified;

import club.flame.disqualified.manager.chat.ChatListener;
import club.flame.disqualified.manager.chat.ChatManager;
import club.flame.disqualified.manager.database.mongo.MongoManager;
import club.flame.disqualified.manager.database.redis.RedisManager;
import club.flame.disqualified.manager.database.redis.payload.Payload;
import club.flame.disqualified.manager.database.redis.payload.RedisMessage;
import club.flame.disqualified.manager.hooks.HookPlaceholderAPI;
import club.flame.disqualified.manager.impl.VaultImpl;
import club.flame.disqualified.manager.listener.BlockCommandListener;
import club.flame.disqualified.manager.listener.GeneralPlayerListener;
import club.flame.disqualified.manager.messages.MessageManager;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.PlayerDataLoad;
import club.flame.disqualified.manager.ranks.RankManager;
import club.flame.disqualified.manager.staff.StaffLang;
import club.flame.disqualified.manager.staff.StaffListener;
import club.flame.disqualified.manager.staff.freeze.FreezeHandlerListener;
import club.flame.disqualified.manager.staff.freeze.FreezeListener;
import club.flame.disqualified.manager.tags.TagManager;
import club.flame.disqualified.manager.tips.TipsRunnable;
import club.flame.disqualified.menu.grant.GrantListener;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.utils.lang.Lang;
import club.flame.disqualified.Permissions;
import club.flame.disqualified.utils.UpdateChecker;
import club.flame.disqualified.lib.DisqualifiedLib;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.config.ConfigCursor;
import club.flame.disqualified.lib.config.FileConfig;
import club.flame.disqualified.lib.menu.ButtonListener;
import club.flame.disqualified.lib.task.TaskUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

@Getter @Setter
public final class Disqualified extends JavaPlugin {

    @Getter private static Disqualified instance;
    private DisqualifiedAPI disqualifiedAPI;
    private FileConfig messagesConfig, databaseConfig, settingsConfig, tagsConfig, ranksConfig, punishmentConfig, commandsFile;
    private DisqualifiedLib disqualifiedLib;
    private TagManager tagManager;
    private MongoManager mongoManager;
    private RedisManager redisManager;
    private MessageManager messageManager;
    private ChatManager chatManager;
    private String disableMessage = "null";
    private RankManager rankManager;
    private boolean joinable = false;
    private boolean passed = false;
    private boolean debug;

    public static String serverName = "%%__USER__%%";

    @Override
    public void onEnable() {
        instance = this;

        if (!this.getDescription().getName().equals("Disqualified")
                || !this.getDescription().getWebsite().equals("https://dsc.gg/flameclubdevelopment")
                || !this.getDescription().getDescription().equals("Disqualified is an All-in-one Network Core")
                || !this.getDescription().getAuthors().contains("FlameClubDevelopment")) {
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            for (int i = 0; i < 100; i++) {
                Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cWhy are you changing the plugin.yml?"));
            }
            return; // Ensure the method exits if the plugin is disabled
        }

        this.disqualifiedAPI = new DisqualifiedAPI();
        this.disqualifiedLib = new DisqualifiedLib(this);
        if (!disqualifiedLib.checkAuthors("FlameClubDevelopment")) {
            return;
        }
        this.commandsFile = new FileConfig(this, "commands.yml");
        this.messagesConfig = new FileConfig(this, "messages.yml");
        this.databaseConfig = new FileConfig(this, "database.yml");
        this.settingsConfig = new FileConfig(this, "settings.yml");
        this.tagsConfig = new FileConfig(this, "tags.yml");
        this.ranksConfig = new FileConfig(this, "ranks.yml");
        this.punishmentConfig = new FileConfig(this, "punishments.yml");
        if (!settingsConfig.getConfiguration().contains("SETTINGS.LICENSE")) {
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cInsert the license..."));
            restartInventoryID();
        }
        Disqualified.getInstance().setPassed(true);
        Bukkit.getConsoleSender().sendMessage(CC.MENU_BAR);
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&4Disqualified Core ✖ &8- &fv" + getDescription().getVersion()));
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cLicense Info"));
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&4Status&f: &aActivated"));
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&4License&f: Open-Source"));
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cSpigot Info"));
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&4Spigot&f: " + getServer().getName() + "&f " + getServer().getVersion()));
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&4Decent spigot detected&f, &ahooked in&f..."));
        Bukkit.getConsoleSender().sendMessage(CC.MENU_BAR);
        plausible();
    }

    private void plausible() {
        this.debug = settingsConfig.getBoolean("SETTINGS.DEBUG");
        this.mongoManager = new MongoManager();
        this.redisManager = new RedisManager();
        this.chatManager = new ChatManager();
        this.tagManager = new TagManager();
        this.messageManager = new MessageManager();
        this.rankManager = new RankManager();

        this.chatManager.load();

        this.mongoManager.connect();
        if (!this.mongoManager.isConnected()) {
            return;
        }

        redisManager.connect();

        this.getLogger().info("Registering ranks...");
        rankManager.loadRanks();

        this.getLogger().info("Registering tags...");
        tagManager.registerTags();

        this.loadCommands();
        this.loadListener();

        if (Disqualified.getInstance().getSettingsConfig().getBoolean("SETTINGS.TIPS.ENABLED")) {
            TaskUtil.runTaskTimerAsynchronously(new TipsRunnable(), Disqualified.getInstance().getSettingsConfig().getInt("SETTINGS.TIPS.DELAY"));
        }

        Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        Bukkit.getConsoleSender().sendMessage(CC.translate("&4Disqualified ✖ &8- &fv" + getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&7 • &4Author&f: " + getDescription().getAuthors()));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&7 • &4Mongo&f: " + (mongoManager.isConnected() ? "&aEnabled" : "&cDisabled")));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&7 • &4Redis&f: " + (redisManager.isActive() ? "&aEnabled" : "&cDisabled")));
        Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);

        if (redisManager.isActive()) {
            String json = new RedisMessage(Payload.SERVER_MANAGER).setParam("SERVER", Lang.SERVER_NAME).setParam("STATUS", "online").toJSON();
            Disqualified.getInstance().getRedisManager().write(json);
        } else {
            String format = CC.translate(Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("NETWORK.SERVER-MANAGER.FORMAT")
                    .replace("<prefix>", CC.translate(Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("NETWORK.SERVER-MANAGER.PREFIX")))
                    .replace("<server>", Lang.SERVER_NAME)
                    .replace("<status>", "&aonline"));
            StaffLang.sendRedisServerMsg(CC.translate(format));
        }

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "Broadcast");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        if (Bukkit.getServer().getVersion() != null) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&7[&4Disqualified&7] &aSpigot hook successfully registered."));
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new HookPlaceholderAPI(this).register();
            Bukkit.getConsoleSender().sendMessage(CC.translate("&7[&4Disqualified&7] &aPlaceholderAPI hook successfully registered."));
        }

        if (this.getServer().getPluginManager().getPlugin("Vault") != null) {
            VaultImpl vault = new VaultImpl();
            vault.register();

            Bukkit.getConsoleSender().sendMessage(CC.translate("&7[&4Disqualified&7] &aVault implementation successfully performed."));
        }

        PlayerData.startTask();
        TaskUtil.runLaterAsync(() -> setJoinable(true), 5 * 20);
    }

    @Override
    public void onDisable() {
        if (!passed) {
            shutdownMessage();
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
            if (playerData != null) {
                playerData.saveData();
            }
        }

        rankManager.saveRanks();

        if (Disqualified.getInstance().getRedisManager().isActive()) {
            String json = new RedisMessage(Payload.SERVER_MANAGER).setParam("SERVER", Lang.SERVER_NAME).setParam("STATUS", "offline").toJSON();
            Disqualified.getInstance().getRedisManager().write(json);
        } else {
            String format = CC.translate(Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("server.format")
                    .replace("<prefix>", Lang.PREFIX)
                    .replace("<server>", Lang.SERVER_NAME)
                    .replace("<status>", "&coffline"));
            StaffLang.sendRedisServerMsg(CC.translate(format));
        }

        mongoManager.disconnect();

        if (redisManager.isActive()) {
            redisManager.disconnect();
        }

        shutdownMessage();
    }

    public void restartInventoryID() {
        Bukkit.shutdown();
        Bukkit.getPluginManager().disablePlugin(this);
        System.exit(0);
    }

    public void reloadTags() {
        this.tagsConfig = new FileConfig(this, "tags.yml");
    }

    private void loadCommands() {
        disqualifiedLib.setExcludeCommandConfig(settingsConfig, "SETTINGS.DISABLE-COMMANDS");
        disqualifiedLib.setDisableCommandMessage(CC.translate(Lang.PREFIX + "&4<command> &fcommand was not registered because it was disabled in the configuration."));
        disqualifiedLib.loadCommandsFromPackage("club.flame.disqualified.command");
        disqualifiedLib.loadCommandsInFile();
    }

    private void loadListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        boolean staffJoinMessages = !new ConfigCursor(messagesConfig, "NETWORK.STAFF-ALERTS").exists("ENABLED") || messagesConfig.getBoolean("NETWORK.STAFF-ALERTS.ENABLED");
        if (staffJoinMessages) {
            pluginManager.registerEvents(new StaffListener(), this);
        }
        registerListeners(
                new PlayerDataLoad(),
                new ChatListener(),
                new ButtonListener(),
                new GeneralPlayerListener(),
                new BlockCommandListener(),
                new GrantListener(),
                new FreezeListener(),
                new FreezeHandlerListener()
        );
    }

    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> {
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(listener, this);
        });
    }

    public void shutdownMessage() {
        if (disableMessage.equalsIgnoreCase("Error in MongoDB")) {
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4Disqualified ✖ &8- &fv" + getDescription().getVersion()));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cAn error has occurred while connecting to MongoDB"));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cMake sure that your database.yml has the right data."));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4Do you need support? &fhttps://dsc.gg/flameclubdevelopment"));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        } else if (disableMessage.equalsIgnoreCase("Not ip whitelist")) {
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4Disqualified ✖ &8- &fv" + getDescription().getVersion()));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cAn error has occurred while verify ip whitelist"));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cIP: " + Utils.getIP() + ":" + this.getServer().getPort()));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4Do you need support? &fhttps://dsc.gg/flameclubdevelopment"));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4Disqualified ✖ &8- &fv" + getDescription().getVersion()));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cDisconnecting plugin..."));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cJoin our discord for any errors."));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4▶ &fhttps://dsc.gg/flameclubdevelopment"));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        }
    }

    public void reloadFile() {
        this.messagesConfig = new FileConfig(this, "messages.yml");
        this.databaseConfig = new FileConfig(this, "database.yml");
        this.settingsConfig = new FileConfig(this, "settings.yml");
    }

    public StringBuilder getAuthors() {
        StringBuilder format = new StringBuilder();
        List<String> authors = getDescription().getAuthors();
        int size = authors.size();
        for (String s : authors) {
            size--;
            if (size == 1) {
                format.append(s);
            } else {
                format.append(", ").append(s);
            }
        }
        return format;
    }
}
