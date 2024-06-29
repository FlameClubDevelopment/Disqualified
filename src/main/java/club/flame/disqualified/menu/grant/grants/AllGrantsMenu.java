package club.flame.disqualified.menu.grant.grants;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.grants.Grant;
import club.flame.disqualified.menu.grant.grants.button.GrantsInfoButton;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.menu.Button;
import club.flame.disqualified.lib.menu.buttons.AirButton;
import club.flame.disqualified.lib.menu.buttons.BackButton;
import club.flame.disqualified.lib.menu.buttons.PageInfoButton;
import club.flame.disqualified.lib.menu.pagination.PageButton;
import club.flame.disqualified.lib.menu.pagination.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class AllGrantsMenu extends PaginatedMenu {

    private PlayerData targetplayerData;

    public AllGrantsMenu(PlayerData playerData){
        this.targetplayerData = playerData;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return CC.translate(ChatColor.valueOf(targetplayerData.getNameColor()) + targetplayerData.getName() + "'s all grants");
    }

    private Comparator<Grant> GRANT_COMPARATOR = Comparator.comparingLong(Grant::getAddedDate).reversed();

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (int i = 0; i < getGrants().size(); i++){
            Grant grant = getGrants().get(i);
            buttons.put(i, new GrantsInfoButton(grant, targetplayerData));
        }

        return buttons;
    }

    private List<Grant> getGrants(){
        List<Grant> grants = new ArrayList<>();
        targetplayerData.getGrants().stream().sorted(GRANT_COMPARATOR).forEach(grants::add);
        return grants;
    }

    @Override
    public void onClose(Player player) {
        PlayerData.deleteOfflineProfile(targetplayerData);
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        // Remove Button
        buttons.put(0, new BackButton(new GrantsMenu(targetplayerData)));
        buttons.put(8, new BackButton(new GrantsMenu(targetplayerData)));
        buttons.put(36, new BackButton(new GrantsMenu(targetplayerData)));
        buttons.put(44, new BackButton(new GrantsMenu(targetplayerData)));

        /*
        First Line Glass Button
         */
        buttons.put(1, new AirButton());
        buttons.put(2, new AirButton());
        buttons.put(3, new AirButton());
        buttons.put(4, new AirButton());
        buttons.put(5, new AirButton());
        buttons.put(6, new AirButton());
        buttons.put(7, new AirButton());
        /*
        Second line button
         */
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
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 9 * 3;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
