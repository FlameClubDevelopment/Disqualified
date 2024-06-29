package club.flame.disqualified.menu.punishments.button;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.punishments.PunishmentType;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.lib.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
public class PlayerAltsButton extends Button {

    private PlayerData targetData;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemCreator itemCreator = new ItemCreator(Material.BEACON);
        itemCreator.setName(ChatColor.valueOf(this.targetData.getNameColor()) + "Alts");
        itemCreator.setLore(altsLore());
        return itemCreator.get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }

    private List<String> altsLore() {
        List<String> strings = new ArrayList<>();
        if (this.targetData.getAlts().isEmpty()) {
            strings.add(CC.translate("&c" + this.targetData.getName() + " doesn't have alts."));
            return strings;
        }

        this.targetData.getAlts().forEach(alts -> {
            if (alts != null) {
                strings.add(CC.translate(Disqualified.getInstance().getPunishmentConfig().getConfiguration().getString("MENU.ALTS-INFO")
                        .replace("<player>", alts.getName() == null ? "None" : alts.getName())
                        .replace("<status>", getStatusPunishment(alts))
                ));
            }
        });

        return strings;
    }

    private String getStatusPunishment(PlayerData playerData) {
        String text;

        if (playerData.getActivePunishment(PunishmentType.BLACKLIST) != null) {
            text = playerData.isOnline() ? CC.translate("&8(&4Blacklist&8) + &8(&aOnline&8)") : CC.translate("&8(&4Blacklist&8) + &8(&cOffline&8)");
        } else if (playerData.getActivePunishment(PunishmentType.BAN) != null) {
            text = playerData.isOnline() ? CC.translate("&8(&cBan&8) + &8(&aOnline&8)") : CC.translate("&8(&cBan&8) + &8(&cOffline&8)");
        } else {
            text = playerData.isOnline() ? CC.translate("&8(&aOnline&8)") : CC.translate("&8(&cOffline&8)");
        }

        return text;
    }
}
