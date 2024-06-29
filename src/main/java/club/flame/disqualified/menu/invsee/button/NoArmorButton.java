package club.flame.disqualified.menu.invsee.button;

import club.flame.disqualified.lib.menu.Button;
import club.flame.disqualified.lib.item.ItemCreator;
import lombok.AllArgsConstructor;
import org.bukkit.Color;
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
public class NoArmorButton extends Button {

    String type;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(getMaterial(type))
                .setName("&7[&4!&7] &cNo " + getMaterial(type).name().toLowerCase().replace("_", " "))
                .setArmorColor(Color.fromRGB(50, 50, 50)).get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }


    private Material getMaterial(String type){
        Material material = null;
        switch (type){
            case "helmet":
                material = Material.LEATHER_HELMET;
                break;
            case "chestplate":
                material = Material.LEATHER_CHESTPLATE;
                break;
            case "leggings":
                material = Material.LEATHER_LEGGINGS;
                break;
            case "boots":
                material = Material.LEATHER_BOOTS;
                break;
        }
        return material;
    }
}
