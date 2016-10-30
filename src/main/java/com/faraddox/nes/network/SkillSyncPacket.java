package com.faraddox.nes.network;

import com.faraddox.nes.gui.NESGui;
import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by faraddox on 28.10.2016.
 */
public class SkillSyncPacket implements IMessage{
    private List<AbstractSkillGroup> skillGroups;

    public SkillSyncPacket(){}
    public SkillSyncPacket(List<AbstractSkillGroup> _skillGroups) {skillGroups = _skillGroups;}

    @SideOnly(Side.CLIENT)
    @Override
    public void fromBytes(ByteBuf buf) {
//        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
//        if (!p.hasCapability(SkillCapability.SKILL_CAPABILITY, SkillCapability.DEFAULT_FACING))
//            MinecraftForge.EVENT_BUS.post(new AttachCapabilitiesEvent(AttachCapabilitiesEvent.class, p));
        skillGroups = SkillCapability.getPlayerSkills(Minecraft.getMinecraft().thePlayer).getPlayerSkillGroups();

//        int skillGroupCount = buf.getInt(index++);
//        for (int i = 0; i < skillGroupCount; i++) {
        for (AbstractSkillGroup sg : skillGroups) {
//            AbstractSkillGroup sg = skillGroups.get(i);
            if (buf.readBoolean()) {
                sg.setXP(buf.readInt());
                sg.setCurrentLevel(buf.readInt());
                sg.setFreePoints(buf.readInt());
                int xplen = buf.readInt();
                int[] xpTable = new int[xplen];
                for (int j = 0; j < xplen; j++)
                    xpTable[j] = buf.readInt();
//                int skillCount = buf.getInt(index++);
//                for (int k = 0; k < skillCount; k++){
                for (Skill skill : sg.getSkills()){
                    if (buf.readBoolean()) {
                        skill.setCurrentPoints(buf.readInt());
//                        skill.setMaxPoints(buf.readInt());
                        buf.readInt();
                    }

                }
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
//        buf.setInt(index++, skillGroups.size());
        for (AbstractSkillGroup sg : skillGroups) {
            buf.writeBoolean(sg.isEnabled());
            if (sg.isEnabled()) {
                buf.writeInt(sg.getXP());
                buf.writeInt(sg.getCurrentLevel());
                buf.writeInt(sg.getFreePoints());
                int[] xpTable = sg.getXpTable();
                buf.writeInt(xpTable.length);
                for (int i = 0; i < xpTable.length; i++)
                    buf.writeInt(xpTable[i]);
                List<Skill> skills = sg.getSkills();
//                buf.setInt(index++, skills.size());
                for (Skill s : skills) {
                    buf.writeBoolean(s.isEnabled());
                    if (s.isEnabled()) {
                        buf.writeInt(s.getCurrentPoints());
                        buf.writeInt(s.getMaxPoints());
                    }
                }
            }
        }
    }

    public static class SkillSyncHandler implements IMessageHandler<SkillSyncPacket, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(SkillSyncPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handle(SkillSyncPacket message, MessageContext ctx) {
            SkillCapability.getPlayerSkills(Minecraft.getMinecraft().thePlayer).setPlayerSkillGroups(message.skillGroups);
            if (Minecraft.getMinecraft().currentScreen instanceof NESGui)
                Minecraft.getMinecraft().currentScreen.updateScreen();
        }
    }
}
