package club.flame.disqualified.menu.punishments.button;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.lib.menu.Button;
import com.mysql.jdbc.Util;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
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
public class PlayerInfoButton extends Button {

    private PlayerData targetData;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemCreator itemCreator = new ItemCreator(Material.SKULL_ITEM, 3);
        itemCreator.setName(ChatColor.valueOf(targetData.getNameColor()) + targetData.getName() + "'s Info");
        itemCreator.setOwner(targetData.getName());
        itemCreator.setLore(getItemLore());

        return itemCreator.get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }

    private List<String> getItemLore() {
        List<String> strings = new ArrayList<>();
        Disqualified.getInstance().getPunishmentConfig().getConfiguration().getStringList("MENU.PLAYER-INFO").forEach(text -> strings.add(CC.translate(text)
                .replace("<player>", this.targetData.getName())
                .replace("<alts>", String.valueOf(this.targetData.getAlts().size()))
                .replace("<country>", getCountry())
        ));

        return strings;
    }

    private String getCountry() {
        if (!this.targetData.isOnline()) {
            return this.targetData.getCountry() == null ? "Not found" : this.targetData.getCountry();
        }
        String ip = this.targetData.getPlayer().getAddress().toString().replaceAll("/", "");
        String country;
        try {
            country = Utils.getCountry(ip);
        } catch (Exception e) {
            country = null;
        }
        return (country == null ? "Not found" : country);
    }
}
