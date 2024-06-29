package club.flame.disqualified.menu.invsee;

import club.flame.disqualified.menu.invsee.button.ItemButton;
import club.flame.disqualified.menu.invsee.button.NoArmorButton;
import club.flame.disqualified.menu.invsee.button.PlayerInfoButton;
import club.flame.disqualified.menu.invsee.button.PotionButton;
import club.flame.disqualified.utils.InventoryUtil;
import club.flame.disqualified.lib.menu.Button;
import club.flame.disqualified.lib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class InventorySeeMenu extends Menu {

    private Player target;

    private BukkitRunnable runnable;

    public InventorySeeMenu(Player player) {
        this.target = player;
    }

    @Override
    public String getTitle(Player player) {
        return "§e" + this.target.getName() + "'s §aInventory";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        ItemStack[] targetContents = InventoryUtil.fixInventoryOrder(this.target.getInventory().getContents());

        for (int i = 0; i < targetContents.length; i++) {
            ItemStack itemStack = targetContents[i];

            if (itemStack != null) {
                if (itemStack.getType() != Material.AIR)
                    buttons.put(i, new ItemButton(itemStack));
            }
        }

        if (this.target.getInventory().getHelmet() == null) {
            buttons.put(36, new NoArmorButton("helmet"));
        } else {
            buttons.put(36, new ItemButton(this.target.getInventory().getHelmet()));
        }

        if (this.target.getInventory().getChestplate() == null) {
            buttons.put(37, new NoArmorButton("chestplate"));
        } else {
            buttons.put(37, new ItemButton(this.target.getInventory().getChestplate()));
        }

        if (this.target.getInventory().getLeggings() == null) {
            buttons.put(38, new NoArmorButton("leggings"));
        } else {
            buttons.put(38, new ItemButton(this.target.getInventory().getLeggings()));
        }

        if (this.target.getInventory().getBoots() == null) {
            buttons.put(39, new NoArmorButton("boots"));
        } else {
            buttons.put(39, new ItemButton(this.target.getInventory().getBoots()));
        }

        if (!this.target.getActivePotionEffects().isEmpty()) {
            buttons.put(42, new PotionButton(this.target));
        }

        buttons.put(43, new PlayerInfoButton(this.target, PlayerInfoButton.Type.HEALTH));
        buttons.put(44, new PlayerInfoButton(this.target, PlayerInfoButton.Type.FOOD));

        setPlaceholder(true);
        setAutoUpdate(true);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}
