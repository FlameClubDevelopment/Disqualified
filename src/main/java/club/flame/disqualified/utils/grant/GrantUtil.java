package club.flame.disqualified.utils.grant;

import club.flame.disqualified.manager.hooks.callback.AbstractCallback;
import club.flame.disqualified.manager.hooks.callback.Callback;
import club.flame.disqualified.manager.hooks.callback.CallbackReason;
import club.flame.disqualified.manager.player.grants.Grant;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.utils.punishment.PunishmentUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class GrantUtil {

    public static List<String> savePlayerGrants(List<Grant> grants) {
        List<String> playerGrants = new ArrayList<>();
        for (Grant grant : grants) {
            if (grant.getRank() != null) {
                playerGrants.add(
                        grant.getRank().getName()
                                + ";" + grant.getAddedDate()
                                + ";" + grant.getDuration()
                                + ";" + grant.getRemovedDate()
                                + ";" + grant.getAddedBy()
                                + ";" + grant.getReason()
                                + ";" + grant.getRemovedBy()
                                + ";" + grant.isActive()
                                + ";" + grant.isPermanent()
                                + ";" + grant.getServer()
                );
            }
        }

        return playerGrants;
    }

    public static List<Grant> getPlayerGrants(List<String> strings) {
        List<Grant> grants = new ArrayList<>();
        for (String string : strings) {
            String[] grantsSplit = string.split(";"); // Split takes each thing starting from a ; - Example: hello;xd[0] returns hello [1] xd
            Grant grant = new Grant(
                    grantsSplit[0],
                    Long.parseLong(grantsSplit[1]),
                    Long.parseLong(grantsSplit[2]),
                    Long.parseLong(grantsSplit[3]),
                    grantsSplit[4],
                    grantsSplit[5],
                    grantsSplit[6],
                    Boolean.parseBoolean(grantsSplit[7]),
                    Boolean.parseBoolean(grantsSplit[8]),
                    grantsSplit[9]
            );
            grants.add(grant);
        }

        return grants;
    }

    public static String getDate(long value) {
        return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date(value));
    }
}
