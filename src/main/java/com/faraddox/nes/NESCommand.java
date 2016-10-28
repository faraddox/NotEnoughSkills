package com.faraddox.nes;

import com.faraddox.nes.skill.*;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.faraddox.nes.util.Chatter.SEND;
import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public class NESCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "nes";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.nes.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        World world = sender.getEntityWorld();
        Entity entity = sender.getCommandSenderEntity();
        String command = "";
        for (String s : args)
            command += s + " ";
        SEND(sender, "Processing:", command);
        if (world.isRemote) {
            SEND(sender, "Not processing on Client side");
        } else {
            SEND(sender, "Processing on server side");
            int count = args.length;
            if (count > 0) {
                if (args[0].equals("get")) {
                    SEND(sender, "Processing get command");
                    if (entity.hasCapability(SkillCapability.SKILL_CAPABILITY, SkillCapability.DEFAULT_FACING)) {
                        SEND(sender, "Player have skills");
                        SkillCapability.ISkillCapability skills = SkillCapability.getPlayerSkills((EntityPlayer)entity);
                        if (count > 2) {
                            SEND(sender, "Processing get command for skill", args[2]);
                            Skill s = skills.getSkillByFullName(NES.MODID + ":" + args[1] + "." + args[2]);
                            skillInfo(sender, s);
                        } else if (count > 1) {
                            SEND(sender, "Processing get command for group", args[1]);
                            AbstractSkillGroup sg = skills.getSkillGroupByFullName(NES.MODID + ":" + args[1]);
                            if (sg != null) {
                                groupInfo(sender, sg);
                                return;
                            }
                        } else {
                            SEND(sender, "Processing get command for all skill groups");
                            skills.getPlayerSkillGroups().forEach((AbstractSkillGroup sg) -> groupInfo(sender, sg));
                            return;
                        }
                    } else {
                        SEND(sender, "Player have not skills or sender is not a player");
                    }
                } else if (args[0].equals("set")) {
                    SEND(sender, "Processing set command");
                }
            }
            SEND(sender, "Invalid arguments");
        }
    }

    public static void skillInfo(ICommandSender sender, Skill s) {
        SEND(sender, "Skill:", s.getSkillName(), "Points:", s.getCurrentPoints(), "/", s.getMaxPoints());
    }

    public static void groupInfo(ICommandSender sender, AbstractSkillGroup sg) {
        SEND(sender, "Skill group:", sg.getGroupName(),
                "Lvl:", sg.getCurrentLevel() + "/" + sg.getMaxLevel(),
                "XP:", sg.getXP() + "/" + sg.getXPForNextLevel());
        sg.getSkills().forEach((Skill s) -> skillInfo(sender, s));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
