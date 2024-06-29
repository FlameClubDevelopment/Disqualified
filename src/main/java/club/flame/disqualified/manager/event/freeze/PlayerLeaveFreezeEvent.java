package club.flame.disqualified.manager.event.freeze;

import club.flame.disqualified.lib.event.PlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@Getter
@Setter
public class PlayerLeaveFreezeEvent extends PlayerEvent implements Cancellable {

    private boolean cancelled;

    /***
     *
     * @param player
     */
    public PlayerLeaveFreezeEvent(Player player) {
        super(player);
    }
}
