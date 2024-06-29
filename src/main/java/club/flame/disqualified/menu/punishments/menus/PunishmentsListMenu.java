package club.flame.disqualified.menu.punishments.menus;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.punishments.Punishment;
import club.flame.disqualified.manager.player.punishments.PunishmentType;
import club.flame.disqualified.menu.punishments.PunishmentFilter;
import club.flame.disqualified.menu.punishments.button.PunishmentInfoButton;
import club.flame.disqualified.lib.menu.Button;
import club.flame.disqualified.lib.menu.buttons.AirButton;
import club.flame.disqualified.lib.menu.buttons.BackButton;
import club.flame.disqualified.lib.menu.buttons.PageInfoButton;
import club.flame.disqualified.lib.menu.pagination.PageButton;
import club.flame.disqualified.lib.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@AllArgsConstructor
public class PunishmentsListMenu extends PaginatedMenu {

    {
        setUpdateAfterClick(true);
    }

    private PlayerData targetData;
    private PunishmentType punishmentType;
    private PunishmentFilter punishmentFilter;

    public static boolean closeBack;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return ChatColor.valueOf(targetData.getNameColor()) + targetData.getName() + " " + punishmentType.getPluralName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (Punishment punishment : this.targetData.getPunishmentsByFilter(punishmentType, punishmentFilter)) {
            buttons.put(buttons.size(), new PunishmentInfoButton(punishment));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        // Remove Button
        buttons.put(0, new BackButton(new PunishmentsMenu(this.targetData)));
        buttons.put(8, new BackButton(new PunishmentsMenu(this.targetData)));
        buttons.put(36, new BackButton(new PunishmentsMenu(this.targetData)));
        buttons.put(44, new BackButton(new PunishmentsMenu(this.targetData)));

        buttons.put(1, new AirButton());
        buttons.put(2, new AirButton());
        buttons.put(3, new AirButton());
        buttons.put(4, new AirButton());
        buttons.put(5, new AirButton());
        buttons.put(6, new AirButton());
        buttons.put(7, new AirButton());
        buttons.put(42, new AirButton());
        buttons.put(41, new AirButton());
        buttons.put(39, new AirButton());
        buttons.put(38, new AirButton());

        // Pages Button
        buttons.put(43, new PageButton(1, this));
        buttons.put(37, new PageButton(-1, this));

        // Page Info Button
        buttons.put(40, new PageInfoButton(this));

        return buttons;
    }

    @Override
    public boolean isPlaceholder() {
        return true;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 9 * 3;
    }
}

