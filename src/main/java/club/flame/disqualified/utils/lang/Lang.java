package club.flame.disqualified.utils.lang;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter
public class Lang {

    public static String TS = Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.SOCIAL.TEAMSPEAK");
    public static String DISCORD = Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.SOCIAL.DISCORD");
    public static String TWITTER = Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.SOCIAL.TWITTER");
    public static String STORE = Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.SOCIAL.STORE");

    public static String SERVER_IP = Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.NAME-MC-CHECK.SERVER-IP");
    public static String SERVER_NAME = Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.SERVER-NAME");
    public static String PREFIX = CC.translate(Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.PREFIX"));

    public static String OFFLINE_PLAYER = CC.translate("&cPlayer not found.");
    public static String NO_PERMS = CC.translate("&cYou don't have permissions.");
    public static String NO_NUMBER = CC.translate("&cIt must be a number");

    public static void playSound(Player player, boolean confirmation) {
        if (confirmation) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
        } else {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
        }
    }
}
