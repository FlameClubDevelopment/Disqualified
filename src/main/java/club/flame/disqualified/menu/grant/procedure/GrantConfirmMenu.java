package club.flame.disqualified.menu.grant.procedure;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.menu.grant.procedure.button.ConfirmCancelButton;
import club.flame.disqualified.menu.grant.procedure.button.GrantInfoButton;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.menu.Button;
import club.flame.disqualified.lib.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class GrantConfirmMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.translate("&aConfirm grant?");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        PlayerData data = PlayerData.getPlayerData(player.getUniqueId());
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new ConfirmCancelButton(ConfirmCancelButton.Type.CANCEL, data));
        buttons.put(13, new GrantInfoButton(data));
        buttons.put(15, new ConfirmCancelButton(ConfirmCancelButton.Type.CONFIRM, data));

        setPlaceholder(true);

        return buttons;
    }

    @Override
    public void onClose(Player player) {
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        if (playerData.getGrantProcedure() != null) {
            PlayerData.deleteOfflineProfile(playerData.getGrantProcedure().getPlayerData());
        }
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}
