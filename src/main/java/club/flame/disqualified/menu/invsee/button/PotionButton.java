package club.flame.disqualified.menu.invsee.button;

import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.lib.menu.Button;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@AllArgsConstructor
public class PotionButton extends Button {

    private Player target;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> potions = new ArrayList<>();
        for (PotionEffect effect : target.getActivePotionEffects()) {
            String potionName = WordUtils.capitalize(effect.getType().getName().replace("_", "").toLowerCase());
            potions.add("§a" + potionName + " " + (effect.getAmplifier() + 1) + " §efor " + Utils.timeCalculate(effect.getDuration() / 20));
        }
        return new ItemCreator(Material.POTION).setName("§ePotions Effects").setLore(potions).get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
