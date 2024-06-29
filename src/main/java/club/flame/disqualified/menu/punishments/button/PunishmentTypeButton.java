package club.flame.disqualified.menu.punishments.button;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.punishments.PunishmentType;
import club.flame.disqualified.menu.punishments.PunishmentFilter;
import club.flame.disqualified.menu.punishments.menus.PunishmentsListMenu;
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
public class PunishmentTypeButton extends Button {

    private PlayerData targetData;
    private PunishmentType punishmentType;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemCreator itemCreator = new ItemCreator(Material.INK_SACK);
        itemCreator.setName(punishmentType.getChatColor() + punishmentType.getPluralName() + " &8(" + punishmentType.getChatColor() + targetData.getPunishmentCountByType(punishmentType) + "&8)");
        itemCreator.setDurability(punishmentType.getColor().getDyeData());
        itemCreator.setAmount(targetData.getPunishmentCountByType(punishmentType));

        return itemCreator.get();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        new PunishmentsListMenu(this.targetData, punishmentType, PunishmentFilter.NONE).openMenu(player);
    }
}
