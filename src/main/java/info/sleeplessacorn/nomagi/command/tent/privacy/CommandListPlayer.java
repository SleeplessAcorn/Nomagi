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

public class CommandListPlayer extends CommandBase {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "list <list> <player>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0)
            throw new CommandException("Not enough args");

        Privacy.ListType listType = Privacy.ListType.valueOf(args[0].toUpperCase(Locale.ENGLISH));
        EntityPlayer toList = getPlayer(server, sender, args[1]);

        TentWorldSavedData tentData = TentWorldSavedData.getData(toList.getEntityWorld());
        Tent tent = tentData.getTent(getCommandSenderAsPlayer(sender).getGameProfile().getId());
        if (tent == null)
            return;

        tent.getPrivacy().listPlayer(listType, toList.getGameProfile().getId());
        tent.markDirty();
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = Lists.newArrayList();
        switch (args.length) {
            case 1: {
                for (Privacy.ListType listType : Privacy.ListType.values())
                    completions.add(listType.name().toLowerCase(Locale.ENGLISH));
                completions = getListOfStringsMatchingLastWord(args, completions);
                break;
            }
            case 2: {
                completions = getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
                break;
            }
        }
        return completions;
    }
}
