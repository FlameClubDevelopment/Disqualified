package club.flame.disqualified.lib.menu.buttons;

import club.flame.disqualified.lib.item.ItemCreator;
import club.flame.disqualified.lib.menu.Button;
import club.flame.disqualified.lib.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

@AllArgsConstructor
public class PageInfoButton extends Button {

    private PaginatedMenu paginatedMenu;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.NETHER_STAR)
                .setName("&ePage Info")
                .setLore(Collections.singletonList("&e" + paginatedMenu.getPage() + "&7/&a" + paginatedMenu.getPages(player)))
                .glow()
                .get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
