package club.flame.disqualified.command.teleport;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.config.ConfigCursor;
import club.flame.disqualified.lib.number.NumberUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportPositionCommand extends BaseCommand {
    @Command(name = "teleportposition", permission = "core.command.tppos", aliases = {"tppos"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Disqualified.getInstance().getMessagesConfig(), "COMMANDS.TELEPORT-MESSAGES");

        if (args.length < 3) {
            p.sendMessage("§cUsage: /" + cmd.getLabel() + " <x> <y> <z>");
            return;
        }
        if (!NumberUtils.checkInt(args[0]) || !NumberUtils.checkInt(args[1]) || !NumberUtils.checkInt(args[2])) {
            p.sendMessage("§ePlease specify a number");
            return;
        }

        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);
        Location loc = new Location(p.getWorld(), x, y, z);
        if (x > 30000000 || x < -30000000 || y > 30000000 || y < -30000000 || z > 30000000 || z < -30000000) {
            p.sendMessage("§eMax value is 30000000/-30000000");
            return;
        }

        p.teleport(loc);
        p.sendMessage(CC.translate(messages.getString("TPPOS")
                .replace("<x>", String.valueOf(x))
                .replace("<y>", String.valueOf(y))
                .replace("<z>", String.valueOf(z)))
        );
    }
}
