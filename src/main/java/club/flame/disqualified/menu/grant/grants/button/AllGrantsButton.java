package club.flame.disqualified.menu.grant.grants.button;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.menu.grant.grants.AllGrantsMenu;
import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.lib.menu.Button;
import lombok.AllArgsConstructor;
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
public class AllGrantsButton extends Button {

    private PlayerData targetData;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.NETHER_STAR).setName("&4Click to view all grants").glow().get();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        new AllGrantsMenu(targetData).openMenu(player);
        playSuccess(player);
    }
}
