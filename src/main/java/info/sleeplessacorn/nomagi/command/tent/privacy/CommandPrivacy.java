package info.sleeplessacorn.nomagi.command.tent.privacy;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

import java.util.List;

public class CommandPrivacy extends CommandTreeBase {

    public CommandPrivacy() {
        addSubcommand(new CommandListPlayer());
        addSubcommand(new CommandUnlistPlayer());
        addSubcommand(new CommandAccessMode());
        addSubcommand(new CommandKnocking());
    }

    @Override
    public String getName() {
        return "privacy";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        List<String> subCommands = Lists.newArrayList();
        for (ICommand subCommand : getSubCommands())
            subCommands.add(subCommand.getName());
        return "privacy [" + Joiner.on("|").join(subCommands) + "]";
    }
}
