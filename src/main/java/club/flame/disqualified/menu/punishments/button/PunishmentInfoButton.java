package club.flame.disqualified.menu.punishments.button;

import club.flame.disqualified.manager.player.punishments.Punishment;
import club.flame.disqualified.lib.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@AllArgsConstructor
public class PunishmentInfoButton extends Button {

    private Punishment punishment;

    @Override
    public ItemStack getButtonItem(Player player) {
        return punishment.toItemStack();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
