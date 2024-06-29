package club.flame.disqualified.lib.commands;

import club.flame.disqualified.lib.DisqualifiedLib;
import org.bukkit.plugin.Plugin;

public abstract class BaseCommand {

    public BaseCommand(){
        DisqualifiedLib.INSTANCE.getCommandFramework().registerCommands(this, null);
    }

    public abstract void onCommand(CommandArgs command);
}