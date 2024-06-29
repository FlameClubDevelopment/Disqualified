package club.flame.disqualified.lib.menu.buttons;

import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.lib.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class CloseButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.INK_SACK).setDurability(1).setName("&cClose").get();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        playNeutral(player);
        player.closeInventory();
    }
}
