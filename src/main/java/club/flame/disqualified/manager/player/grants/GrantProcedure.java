package club.flame.disqualified.manager.player.grants;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.utils.Utils;
import lombok.Getter;
import lombok.Setter;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@Getter @Setter
public class GrantProcedure {
    private PlayerData playerData;

    private GrantProcedureState grantProcedureState = GrantProcedureState.START;

    private long enteredDuration;

    private String enteredReason;

    private String rankName;

    private String server;

    private boolean permanent = false;

    public GrantProcedure(PlayerData playerData){
        this.playerData = playerData;
    }

    public String getNiceDuration() {
        if (isPermanent())
            return "Permanent";
        return Utils.formatTimeMillis(this.enteredDuration);
    }
}
