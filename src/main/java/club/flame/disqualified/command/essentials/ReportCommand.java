package club.flame.disqualified.command.essentials;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.database.redis.payload.Payload;
import club.flame.disqualified.manager.database.redis.payload.RedisMessage;
import club.flame.disqualified.manager.event.PlayerReportEvent;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.staff.StaffLang;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.utils.lang.Lang;
import club.flame.disqualified.utils.time.Cooldown;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class ReportCommand extends BaseCommand {
    @Command(name = "report",permission = "core.essentials.report",inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        PlayerData playerData = PlayerData.getPlayerData(p.getUniqueId());
        Cooldown cooldown = new Cooldown(Disqualified.getInstance().getSettingsConfig().getConfiguration().getInt("SETTINGS.REPORT.COOLDOWN"));

        if (args.length == 0){
            p.sendMessage(CC.translate("&cUsage: /" + cmd.getLabel() + " <player> <message>"));
            return;
        }
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null){
            p.sendMessage("§cCouldn't find player");
            return;
        }
        if (targetPlayer == p){
            p.sendMessage(CC.translate("&cYou can't report yourself"));
            return;
        }
        if (args.length < 2){
            p.sendMessage(CC.translate("&cPlease specify a message"));
            return;
        }
        if (!playerData.getReportCooldown().hasExpired()){
            p.sendMessage(CC.translate(Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.REPORT.MSG.COOLDOWN")
                    .replace("<time>",playerData.getReportCooldown().getTimeMilisLeft())
                    .replace("<left>",playerData.getReportCooldown().getContextLeft())));
            return;
        }
        String text = StringUtils.join(args, ' ', 1, args.length);
        PlayerReportEvent playerReportEvent = new PlayerReportEvent(p,targetPlayer.getName(),text);
        Bukkit.getPluginManager().callEvent(playerReportEvent);
        if (playerReportEvent.isCancelled()) return;

        playerData.setReportCooldown(cooldown);

        p.sendMessage(CC.translate(Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.REPORT.MSG.SENDER")
                .replace("<target>",targetPlayer.getName())
                .replace("<text>",text)));

        if (Disqualified.getInstance().getRedisManager().isActive()){
            String json = new RedisMessage(Payload.REPORT)
                    .setParam("SENDER",p.getName())
                    .setParam("TARGET",targetPlayer.getName())
                    .setParam("SERVER",Lang.SERVER_NAME)
                    .setParam("REASON", text).toJSON();
            Disqualified.getInstance().getRedisManager().write(json);
        } else {
            StaffLang.sendReport(p.getName(),targetPlayer.getName(),Lang.SERVER_NAME,text);
        }
    }
}
