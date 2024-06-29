package club.flame.disqualified.menu.grant.procedure.button;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.database.redis.payload.Payload;
import club.flame.disqualified.manager.database.redis.payload.RedisMessage;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.grants.Grant;
import club.flame.disqualified.manager.ranks.Rank;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.utils.lang.Lang;
import club.flame.disqualified.lib.menu.Button;
import club.flame.disqualified.lib.task.TaskUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@AllArgsConstructor
public class ConfirmCancelButton extends Button {

    private Type type;

    private PlayerData data;

    @Override
    public ItemStack getButtonItem(Player player) {
        return getItem(type);
    }

    private ItemStack getItem(Type type) {
        ItemStack itemStack = null;
        switch (type) {
            case CONFIRM:
                itemStack = new ItemCreator(Material.INK_SACK).setDurability(2).setName("&aConfirm Grant").get();
                break;
            case CANCEL:
                itemStack = new ItemCreator(Material.INK_SACK, 1).setName("&cCancel Grant").get();
                break;
        }
        return itemStack;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        switch (type) {
            case CANCEL:
                playNeutral(player);
                player.closeInventory();
                break;
            case CONFIRM:
                Rank rankData = Rank.getRankByName(data.getGrantProcedure().getRankName());
                if (rankData == null) {
                    player.closeInventory();
                    player.sendMessage("&4Error! &cThat rank doesn't exist.");
                    return;
                }
                Grant grant = new Grant(null, 1L, 1L, 1L, "", "", "", false, false, "Global");
                grant.setRankName(rankData.getName());
                grant.setActive(true);
                grant.setServer(data.getGrantProcedure().getServer());
                grant.setAddedDate(System.currentTimeMillis());
                grant.setAddedBy(player.getName());
                grant.setDuration(data.getGrantProcedure().getEnteredDuration());
                grant.setPermanent(data.getGrantProcedure().isPermanent());
                grant.setReason(data.getGrantProcedure().getEnteredReason());
                player.closeInventory();
                TaskUtil.runAsync(() -> {
                    PlayerData targetData = PlayerData.getPlayerData(data.getGrantProcedure().getPlayerData().getUuid());
                    if (targetData == null) {
                        targetData = PlayerData.loadData(data.getGrantProcedure().getPlayerData().getUuid());
                    }
                    if (targetData == null) return;
                    targetData.getGrants().add(grant);
                    playSuccess(player);
                    if (grant.isPermanent()) {
                        player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Added permanently grant " + grant.getRank().getName() + " to " + targetData.getName()));
                        String json = new RedisMessage(Payload.GRANT_ADD)
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
                        player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Added permanently grant " + grant.getRank().getName() + " to " + targetData.getName() + " for " + grant.getNiceDuration()));
                        String json = new RedisMessage(Payload.GRANT_ADD)
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
                        String json = new RedisMessage(Payload.GRANT_UPDATE)
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
                        PlayerData targetPlayerData = PlayerData.getPlayerData(target.getUniqueId());
                        targetPlayerData.loadPermissions(target);
                    }
                    if (targetData.isOnline()) {
                        targetData.saveData();
                    } else {
                        PlayerData.deleteOfflineProfile(targetData);
                    }
                });
                break;
        }
    }

    public enum Type {
        CONFIRM,
        CANCEL
    }
}
