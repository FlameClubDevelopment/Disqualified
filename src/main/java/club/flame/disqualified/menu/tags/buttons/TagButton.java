package club.flame.disqualified.menu.tags.buttons;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.tags.Tag;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.lib.menu.Button;
import lombok.AllArgsConstructor;
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
public class TagButton extends Button {

    private final Tag tag;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemCreator itemCreator = new ItemCreator(tag.getTagIcon());
        itemCreator.setName(tag.getTagDisplayName());
        List<String> lore = new ArrayList<>();
        if (player.hasPermission(tag.getTagPermission()) || player.hasPermission("core.tags.all")) {
            for (String msg : tag.getTagLore()) {
                lore.add(CC.translate(msg.replace("<player>", player.getName()).replace("<tag>", tag.getTagPrefix())));
            }
        } else {
            for (String msg : Disqualified.getInstance().getTagsConfig().getConfiguration().getStringList("no-perms-lore")) {
                lore.add(CC.translate(msg.replace("<player>", player.getName()).replace("<tag>", tag.getTagPrefix())));
            }
        }

        itemCreator.setLore(lore);
        itemCreator.setAmount(1);

        return itemCreator.get();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        PlayerData data = PlayerData.getPlayerData(player.getUniqueId());
        if (player.hasPermission(tag.getTagPermission()) || player.hasPermission("core.tags.all")) {
            data.setTag(tag.getTagPrefix());
            player.sendMessage("§aTag successfully selected");
            playSuccess(player);
        } else {
            player.sendMessage(CC.translate("§cYou don't have this tag"));
            playNeutral(player);
        }

        player.closeInventory();
    }
}
