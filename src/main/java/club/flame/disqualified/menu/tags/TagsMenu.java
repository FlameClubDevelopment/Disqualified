package club.flame.disqualified.menu.tags;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.tags.Tag;
import club.flame.disqualified.menu.tags.buttons.RemoveTagButton;
import club.flame.disqualified.menu.tags.buttons.TagButton;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.menu.Button;
import club.flame.disqualified.lib.menu.buttons.AirButton;
import club.flame.disqualified.lib.menu.buttons.PageInfoButton;
import club.flame.disqualified.lib.menu.pagination.PageButton;
import club.flame.disqualified.lib.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class TagsMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return CC.translate(Disqualified.getInstance().getTagsConfig().getConfiguration().getString("title"));
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < Disqualified.getInstance().getTagManager().getTags().size(); i++) {
            Tag tag = Disqualified.getInstance().getTagManager().getTags().get(i);
            buttons.put(i, new TagButton(tag));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        // Remove Button
        buttons.put(0, new RemoveTagButton());
        buttons.put(8, new RemoveTagButton());
        buttons.put(36, new RemoveTagButton());
        buttons.put(44, new RemoveTagButton());

        // First line of glass buttons
        buttons.put(1, new AirButton());
        buttons.put(2, new AirButton());
        buttons.put(3, new AirButton());
        buttons.put(4, new AirButton());
        buttons.put(5, new AirButton());
        buttons.put(6, new AirButton());
        buttons.put(7, new AirButton());

        // Second line of glass buttons
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
