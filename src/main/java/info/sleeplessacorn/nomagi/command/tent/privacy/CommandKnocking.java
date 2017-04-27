package info.sleeplessacorn.nomagi.command.tent.privacy;

import com.google.common.collect.Lists;
import info.sleeplessacorn.nomagi.core.data.Privacy;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class CommandKnocking extends CommandBase {

    @Override
    public String getName() {
        return "knocking";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "access <true|false>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1)
            throw new CommandException("Not enough args");

        EntityPlayer player = getCommandSenderAsPlayer(sender);
        TentWorldSavedData tentData = TentWorldSavedData.getData(player.getEntityWorld());
        Tent tent = tentData.getTent(player.getGameProfile().getId());
        tent.getPrivacy().setKnockable(Boolean.parseBoolean(args[0]));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = Lists.newArrayList();
        switch (args.length) {
            case 1: {
                for (Privacy.AccessMode mode : Privacy.AccessMode.values())
                    completions.add(mode.name().toLowerCase(Locale.ENGLISH));
                completions = getListOfStringsMatchingLastWord(args, completions);
                break;
            }
        }
        return completions;
    }
}
