package club.flame.disqualified.manager.ranks;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.database.redis.payload.Payload;
import club.flame.disqualified.manager.database.redis.payload.RedisMessage;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.grants.Grant;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.config.FileConfig;
import club.flame.disqualified.utils.lang.Lang;
import club.flame.disqualified.lib.task.TaskUtil;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class RankManager {

    public void loadRanks() {
        if (Disqualified.getInstance().getMongoManager().getRanksData().find().into(new ArrayList<>()).isEmpty()) {
            loadRanksFromConfig();
        } else {
            loadRanksFromMongo();
        }
    }

    public void loadRanksFromConfig() {
        Rank.ranks.clear();
        try {
            for (String rank : Disqualified.getInstance().getRanksConfig().getConfiguration().getKeys(false)) {
                String rankName = Disqualified.getInstance().getRanksConfig().getConfiguration().getString(rank + ".NAME");
                String rankPrefix = Disqualified.getInstance().getRanksConfig().getConfiguration().getString(rank + ".PREFIX");
                String rankSuffix = Disqualified.getInstance().getRanksConfig().getConfiguration().getString(rank + ".SUFFIX");
                ChatColor rankColor = ChatColor.valueOf(Disqualified.getInstance().getRanksConfig().getConfiguration().getString(rank + ".COLOR"));
                if (rankColor == null) {
                    rankColor = ChatColor.WHITE;
                }
                boolean rankDefault = Disqualified.getInstance().getRanksConfig().getConfiguration().getBoolean(rank + ".DEFAULT");
                boolean rankBold = Disqualified.getInstance().getRanksConfig().getConfiguration().getBoolean(rank + ".BOLD");
                boolean rankItalic = Disqualified.getInstance().getRanksConfig().getConfiguration().getBoolean(rank + ".ITALIC");
                int rankPriority = Disqualified.getInstance().getRanksConfig().getConfiguration().getInt(rank + ".PRIORITY");
                List<String> rankInheritance = Disqualified.getInstance().getRanksConfig().getConfiguration().getStringList(rank + ".INHERITANCE");
                List<String> rankPermission = Disqualified.getInstance().getRanksConfig().getConfiguration().getStringList(rank + ".PERMISSIONS");

                new Rank(rankName, rankPrefix, rankSuffix, rankColor, rankPriority, rankDefault, rankBold, rankItalic, rankPermission, rankInheritance);
            }
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§eSuccessfully loaded §f" + Rank.ranks.size() + " §eranks from Ranks.yml");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§cAn error occurred while loading the ranks. Please check your config!");
            Bukkit.shutdown();
        }
    }

    public void saveRanks() {
        Document document = new Document();
        try {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§aSaving ranks in MongoDB.");
            for (Rank rank : Rank.ranks) {
                document.put("NAME", rank.getName());
                document.put("PREFIX", rank.getPrefix());
                document.put("SUFFIX", rank.getSuffix());
                document.put("COLOR", rank.getColor().name());
                document.put("PRIORITY", rank.getPriority());
                document.put("DEFAULT", rank.isDefaultRank());
                document.put("BOLD", rank.isBold());
                document.put("ITALIC", rank.isItalic());
                document.put("INHERITANCE", rank.getInheritance());
                document.put("PERMISSIONS", rank.getPermissions());

                Disqualified.getInstance().getMongoManager().getRanksData().replaceOne(Filters.eq("NAME", rank.getName()), document, (new UpdateOptions()).upsert(true));
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§cAn error occurred while saving the ranks. Please check your config!");
        }
    }

    public void loadRanksFromMongo() {
        Rank.ranks.clear();
        try {
            List<Document> documents = Disqualified.getInstance().getMongoManager().getRanksData().find().into(new ArrayList<>());
            for (Document document : documents) {
                String rankName = document.getString("NAME");
                String rankPrefix = document.getString("PREFIX");
                String rankSuffix = document.getString("SUFFIX");
                ChatColor rankColor = ChatColor.valueOf(document.getString("COLOR"));
                if (rankColor == null) {
                    rankColor = ChatColor.WHITE;
                }
                boolean rankDefault = document.getBoolean("DEFAULT");
                boolean rankBold = document.getBoolean("BOLD");
                boolean rankItalic = document.getBoolean("ITALIC");
                int rankPriority = document.getInteger("PRIORITY");

                List<String> rankInheritance = (List<String>) document.get("INHERITANCE");
                List<String> rankPermission = (List<String>) document.get("PERMISSIONS");
                new Rank(rankName, rankPrefix, rankSuffix, rankColor, rankPriority, rankDefault, rankBold, rankItalic, rankPermission, rankInheritance);
            }
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§eSuccessfully loaded §f" + Rank.ranks.size() + " §eranks from MongoDB");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§cAn error occurred while loading the ranks. Please check your MongoDB!");
            Bukkit.shutdown();
        }
    }

    public void saveFromMongo() {
        List<Document> documents = Disqualified.getInstance().getMongoManager().getRanksData().find().into(new ArrayList<>());
        for (Document document : documents) {
            String rankName = document.getString("NAME");
            String rankPrefix = document.getString("PREFIX");
            String rankSuffix = document.getString("SUFFIX");
            ChatColor rankColor = ChatColor.valueOf(document.getString("COLOR"));
            if (rankColor == null) {
                rankColor = ChatColor.WHITE;
            }
            boolean rankDefault = document.getBoolean("DEFAULT");
            boolean rankBold = document.getBoolean("BOLD");
            boolean rankItalic = document.getBoolean("ITALIC");
            int rankPriority = document.getInteger("PRIORITY");
            List<String> rankPermission = (List<String>) document.get("PERMISSIONS");
            List<String> rankInheritance = (List<String>) document.get("INHERITANCE");

            FileConfig config = Disqualified.getInstance().getRanksConfig();

            config.getConfiguration().set(rankName + ".NAME", rankName);
            config.getConfiguration().set(rankName + ".PREFIX", rankPrefix);
            config.getConfiguration().set(rankName + ".SUFFIX", rankSuffix);
            config.getConfiguration().set(rankName + ".COLOR", rankColor.name());
            config.getConfiguration().set(rankName + ".DEFAULT", rankDefault);
            config.getConfiguration().set(rankName + ".BOLD", rankBold);
            config.getConfiguration().set(rankName + ".ITALIC", rankItalic);
            config.getConfiguration().set(rankName + ".PRIORITY", rankPriority);
            config.getConfiguration().set(rankName + ".INHERITANCE", rankInheritance);
            config.getConfiguration().set(rankName + ".PERMISSIONS", rankPermission);

            config.save();
        }
    }

    public Rank getDefaultRank() {
        List<Rank> ranks = new ArrayList<>(Rank.ranks);
        List<Rank> defaults = ranks.stream().sorted(Comparator.comparingInt(Rank::getPriority).reversed()).filter(Rank::isDefaultRank).collect(Collectors.toList());
        if (defaults.size() == 0) {
            List<String> perms = new ArrayList<>();
            List<String> inheritance = new ArrayList<>();
            Rank defaultRank = new Rank("Default", "", "", ChatColor.WHITE, 50, true, false, false, perms, inheritance);
            defaultRank.setDefaultRank(true);
            return defaultRank;
        }

        return defaults.get(0);
    }

    public void updateRank(Rank rank) {
        if (rank != null) {
            rank.update();
        }

        String json = new RedisMessage(Payload.RANK_UPDATE_PERMS).setParam("RANK", rank.getName()).toJSON();
        if (Disqualified.getInstance().getRedisManager().isActive()) {
            Disqualified.getInstance().getRedisManager().write(json);
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> {
                PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
                if (playerData != null) {
                    if (rank != null) {
                        if (playerData.hasRank(rank)) {
                            playerData.refreshPlayer(playerData.getPlayer());
                        }
                    }
                }
            });
        }
    }

    public void deleteRank(Rank rank) {
        String json = new RedisMessage(Payload.RANK_DELETE).setParam("RANK", rank.getName()).toJSON();
        Disqualified.getInstance().getRedisManager().write(json);
    }

    public boolean canGrant(PlayerData playerData, Rank rank) {
        Rank granterRank = playerData.getHighestRank();
        return (granterRank.getPriority() > rank.getPriority());
    }

    public void giveRank(CommandSender sender, PlayerData targetData, long duration, boolean permanent, String reason, Rank rankData, String server) {
        Grant grant = new Grant(null, 1L, 1L, 1L, "", "", "", false, false, "Global");
        grant.setRankName(rankData.getName());
        grant.setActive(true);
        grant.setAddedDate(System.currentTimeMillis());
        grant.setAddedBy(sender.getName());
        grant.setDuration(duration);
        grant.setPermanent(permanent);
        grant.setReason(reason);
        grant.setServer(server);
        TaskUtil.runAsync((() -> {
            String json = null;
            targetData.getGrants().add(grant);
            if (grant.isPermanent()) {
                sender.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Added permanently grant " + grant.getRank().getName() + " to " + targetData.getName()));
                json = new RedisMessage(Payload.GRANT_ADD)
                        .setParam("NAME", targetData.getName())
                        .setParam("MESSAGE", CC.translate(Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.GRANT.PERM").replace("<rank>", rankData.getName()))).toJSON();
                if (Disqualified.getInstance().getRedisManager().isActive()) {
                    Disqualified.getInstance().getRedisManager().write(json);
                } else {
                    if (targetData.getPlayer() != null && targetData.getPlayer().isOnline()) {
                        targetData.getPlayer().sendMessage(CC.translate(Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.GRANT.PERM").replace("<rank>", rankData.getName())));
                    }
                }
            } else {
                sender.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Added permanently grant " + grant.getRank().getName() + " to " + targetData.getName() + " for " + grant.getNiceDuration()));
                json = new RedisMessage(Payload.GRANT_ADD)
                        .setParam("NAME", targetData.getName())
                        .setParam("MESSAGE", CC.translate(Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.GRANT.TEMP")
                                .replace("<time>", grant.getNiceDuration())
                                .replace("<rank>", rankData.getName()))).toJSON();
                if (Disqualified.getInstance().getRedisManager().isActive()) {
                    Disqualified.getInstance().getRedisManager().write(json);
                } else {
                    if (targetData.getPlayer() != null && targetData.getPlayer().isOnline()) {
                        targetData.getPlayer().sendMessage(CC.translate(Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.GRANT.TEMP")
                                .replace("<time>", grant.getNiceDuration())
                                .replace("<rank>", rankData.getName())));
                    }
                }
            }

            Player target = Bukkit.getPlayer(targetData.getName());
            if (target == null) {
                json = new RedisMessage(Payload.GRANT_UPDATE)
                        .setParam("NAME", targetData.getName())
                        .setParam("GRANT", grant.getRank().getName()
                                + ";" + grant.getAddedDate()
                                + ";" + grant.getDuration()
                                + ";" + grant.getRemovedDate()
                                + ";" + grant.getAddedBy()
                                + ";" + grant.getReason()
                                + ";" + grant.getRemovedBy()
                                + ";" + grant.isActive()
                                + ";" + grant.isPermanent()
                                + ";" + grant.getServer()).toJSON();
                if (Disqualified.getInstance().getRedisManager().isActive()) {
                    Disqualified.getInstance().getRedisManager().write(json);
                }
            } else {
                PlayerData playerData = PlayerData.getPlayerData(target.getUniqueId());
                playerData.loadPermissions(target);
            }
        }));
    }
}
