package club.flame.disqualified.lib.menu.buttons;

import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.lib.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class AirButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.AIR).get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
