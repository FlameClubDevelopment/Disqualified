package club.flame.disqualified.menu.color.namecolor.buttons;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.lib.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@AllArgsConstructor
public class ItalicBoldButton extends Button {

    private int type;

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerData data = PlayerData.getPlayerData(player.getUniqueId());
        return getMaterial(type, data);
    }

    private ItemStack getMaterial(int i, PlayerData playerData) {
        ChatColor color;
        if (playerData.getNameColor() != null) {
            color = ChatColor.valueOf(playerData.getNameColor());
        } else {
            color = ChatColor.valueOf(Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.CHAT.FORMAT.DEFAULT-COLOR"));
        }
        if (i == 1) {
            return new ItemCreator(Material.IRON_DOOR)
                    .setName(color + "&oItalic")
                    .setLore(Collections.singletonList((playerData.isItalic() ? "&aenabled" : "&cdisabled")))
                    .get();
        } else {
            return new ItemCreator(Material.SIGN)
                    .setName(color + "&lBold")
                    .setLore(Collections.singletonList((playerData.isBold() ? "&aenabled" : "&cdisabled")))
                    .get();
        }
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        PlayerData data = PlayerData.getPlayerData(player.getUniqueId());
        switch (this.type) {
            case 1:
                if (player.hasPermission("core.namecolor.italic")) {
                    playNeutral(player);
                    data.setItalic(!data.isItalic());
                } else {
                    player.sendMessage("§cYou don't have permission for this option");
                    playNeutral(player);
                    player.closeInventory();
                }
                break;
            case 2:
                if (player.hasPermission("core.namecolor.bold")) {
                    playSuccess(player);
                    data.setBold(!data.isBold());
                } else {
                    player.sendMessage("§cYou don't have permission for this option");
                    playNeutral(player);
                    player.closeInventory();
                }
                break;
            default:
                break;
        }
    }
}
