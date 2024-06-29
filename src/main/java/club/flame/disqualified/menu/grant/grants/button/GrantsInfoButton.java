package club.flame.disqualified.menu.grant.grants.button;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.database.redis.payload.Payload;
import club.flame.disqualified.manager.database.redis.payload.RedisMessage;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.grants.Grant;
import club.flame.disqualified.manager.ranks.Rank;
import club.flame.disqualified.utils.grant.GrantUtil;
import club.flame.disqualified.utils.grant.WoolUtil;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.lib.menu.Button;
import club.flame.disqualified.lib.task.TaskUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@AllArgsConstructor
public class GrantsInfoButton extends Button {

    private Grant grant;
    private PlayerData targetPlayerData;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemCreator itemCreator = new ItemCreator(Material.WOOL);
        Rank rank = grant.getRank();
        itemCreator.setName(grant.getRank().getColor() + grant.getRank().getName());
        if (rank != null) {
            itemCreator.setName(rank.getColor() + grant.getRank().getName());
        } else {
            itemCreator.setName("&4" + grant.getRankName());
        }

        List<String> lore = new ArrayList<>();
        Disqualified.getInstance().getMessagesConfig().getConfiguration().getStringList("COMMANDS.GRANT.GRANT-MENU.GRANTS.LORE").forEach(text -> {
            switch (text) {
                case "<not-expired>":
                    if (!grant.hasExpired() && grant.getRank() != null && !grant.getRank().isDefaultRank())
                        Disqualified.getInstance().getMessagesConfig().getConfiguration().getStringList("COMMANDS.GRANT.GRANT-MENU.GRANTS.NOT-EXPIRED").forEach(textExpired -> lore.add(CC.translate(textExpired)));
                    break;
                case "<if-removed>":
                    if (grant.getRemovedBy() != null && !grant.getRemovedBy().equalsIgnoreCase(""))
                        Disqualified.getInstance().getMessagesConfig().getConfiguration().getStringList("COMMANDS.GRANT.GRANT-MENU.GRANTS.IF-REMOVED").forEach(textRemoved -> lore.add(CC.translate(textRemoved)
                                .replace("<removedBy>", grant.getRemovedBy())
                                .replace("<removedDate>", GrantUtil.getDate(grant.getRemovedDate()))));
                    break;
                default:
                    lore.add(translate(grant, text));
                    break;
            }
        });

        itemCreator.setLore(lore);
        itemCreator.setDurability(WoolUtil.convertChatColorToWoolData(grant.getRank().getColor()));

        return itemCreator.get();
    }

    private String translate(Grant grant, String text) {
        text = CC.translate(text);

        text = text
                .replace("<addedBy>", grant.getAddedBy())
                .replace("<addedDate>", GrantUtil.getDate(grant.getAddedDate()))
                .replace("<duration>", grant.getNiceDuration())
                .replace("<reason>", grant.getReason())
                .replace("<active>", !grant.hasExpired() ? "&aYes" : "&cNo")
                .replace("<expire>", grant.getNiceExpire());

        return text;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        Rank rank = grant.getRank();
        if (rank != null && rank.isDefaultRank()) return;
        if (grant.hasExpired()) return;

        grant.setActive(false);
        grant.setRemovedDate(System.currentTimeMillis());
        grant.setRemovedBy(player.getName());
        TaskUtil.runAsync(() -> {
            Player target = Bukkit.getPlayer(targetPlayerData.getName());
            if (target == null) {
                String json = new RedisMessage(Payload.GRANT_UPDATE)
                        .setParam("NAME", targetPlayerData.getName())
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
                targetPlayerData.loadPermissions(target);
            }
        });
    }
}
