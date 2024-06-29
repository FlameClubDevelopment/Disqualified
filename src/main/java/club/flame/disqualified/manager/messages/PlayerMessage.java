package club.flame.disqualified.manager.messages;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import lombok.Getter;
import lombok.Setter;
import club.flame.disqualified.manager.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter @Setter
public class PlayerMessage {

    private Player sender;
    private Player target;
    private String message;
    private boolean reply;

    public PlayerMessage(Player sender, Player target, String message, boolean reply) {
        this.sender = sender;
        this.target = target;
        this.message = message;
        this.reply = reply;
    }
    /*
    Te falto el SETTINGS xde
     */

    public void send() {
        Disqualified.getInstance().getMessageManager().getLastReplied().put(sender.getUniqueId(), target.getUniqueId());
        Disqualified.getInstance().getMessageManager().getLastReplied().put(target.getUniqueId(), sender.getUniqueId());
        
        PlayerData targetData = PlayerData.getPlayerData(target.getUniqueId());
        PlayerData senderData = PlayerData.getPlayerData(sender.getUniqueId());

        String senderFormat = Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.PRIVATE-MESSAGES.FORMAT.SENDER")
                .replace("<target>", this.target.getName())
                .replace("<rankName>", targetData.getHighestRank().getName())
                .replace("<rankPrefix>", targetData.getHighestRank().getPrefix())
                .replace("<rankColor>", targetData.getHighestRank().getColor().toString())
                .replace("<text>", this.message);
        String targetFormat = Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.PRIVATE-MESSAGES.FORMAT.TARGET")
                .replace("<sender>", this.sender.getName())
                .replace("<rankName>", senderData.getHighestRank().getName())
                .replace("<rankPrefix>", senderData.getHighestRank().getPrefix())
                .replace("<rankColor>", senderData.getHighestRank().getColor().toString())
                .replace("<text>", this.message);
        String socialSpyFormat = Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.PRIVATE-MESSAGES.FORMAT.SOCIAL-SPY")
                .replace("<sender>", this.sender.getName())
                .replace("<senderRankName>", senderData.getHighestRank().getName())
                .replace("<senderRankPrefix>", senderData.getHighestRank().getPrefix())
                .replace("<senderRankColor>", senderData.getHighestRank().getColor().toString())
                
                .replace("<targetRankName>", targetData.getHighestRank().getName())
                .replace("<targetRankPrefix>", targetData.getHighestRank().getPrefix())
                .replace("<targetRankColor>", targetData.getHighestRank().getColor().toString())
                .replace("<target>", this.target.getName())
                .replace("<text>", this.message);
        
        if (targetData.isToggleSounds()) {
            String sound = Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.PRIVATE-MESSAGES.NOTIFICATION-SOUND");
            if (!(sound.equals("none") || sound.equals("NONE") || sound == null)) {
                this.target.playSound(this.target.getLocation(), Sound.valueOf(sound), 2F, 2F);
            }
        }

        this.sender.sendMessage(CC.translate(senderFormat));
        this.target.sendMessage(CC.translate(targetFormat));

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerData.getPlayerData(p.getUniqueId());
            if (data == null) return;
            if (data.isSocialSpy() && p.hasPermission("core.chat.socialSpy")) {
                p.sendMessage(CC.translate(socialSpyFormat));
            }
        }
    }
}
