package club.flame.disqualified.menu.invsee.button;

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
public class ItemButton extends Button {

    private ItemStack itemStack;

    @Override
    public ItemStack getButtonItem(Player player) {
        return this.itemStack;
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
