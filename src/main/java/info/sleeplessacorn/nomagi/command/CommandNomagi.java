package info.sleeplessacorn.nomagi.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

import java.util.List;

public class CommandNomagi extends CommandTreeBase {

    public CommandNomagi() {
        addSubcommand(new CommandTeleportDim());
    }

    @Override
    public String getName() {
        return "nomagi";
    }

    @Override
    public String getUsage(ICommandSender sender) {

        List<String> subCommands = Lists.newArrayList();
        for (ICommand subCommand : getSubCommands())
            subCommands.add(subCommand.getName());
        return "nomagi [" + Joiner.on("|").join(subCommands) + "]";
    }
}
