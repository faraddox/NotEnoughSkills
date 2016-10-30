package com.faraddox.nes.network;

import com.faraddox.nes.NESConfig;
import com.faraddox.nes.gui.NESGuiSkill;
import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * Created by faraddox on 30.10.2016.
 */
public class SpendPointsPacket implements IMessage{
    private Map<String, Integer> spendedPoints;

    public SpendPointsPacket(){}

    public SpendPointsPacket(Map<String, Integer> sp) {
        spendedPoints = sp;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        spendedPoints = new HashMap<>();
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            int len = buf.readInt();
            char[] c = new char[len];
            for (int j = 0; j < len; j++)
                c[j] = buf.readChar();
            String name = new String(c);
            spendedPoints.put(name, buf.readInt());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(spendedPoints.keySet().size());
        spendedPoints.forEach((String name, Integer i) -> {
            buf.writeInt(name.length());
            for (char c: name.toCharArray())
                buf.writeChar(c);
            buf.writeInt(i);
        });
    }

    public static class SpendPointsHandler implements IMessageHandler<SpendPointsPacket, IMessage> {

        @Override
        public IMessage onMessage(SpendPointsPacket message, MessageContext ctx) {
            ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(SpendPointsPacket message, MessageContext ctx) {
            SkillCapability.getPlayerSkills(ctx.getServerHandler().playerEntity).trySpendPoints(message.spendedPoints);
        }
    }
}
