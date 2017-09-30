package info.sleeplessacorn.nomagi.command;

import com.google.common.base.Joiner;
import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandNomagi extends CommandTreeBase {

    public CommandNomagi() {
        addSubcommand(new CommandTeleportDim());
        // TODO Tent sub-commands
    }

    @Override
    public String getName() {
        return Nomagi.ID;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return Nomagi.ID + " [" + Joiner.on("|").join(
                getSubCommands().stream().map(ICommand::getName).iterator()
        ) + "]";
    }
}
