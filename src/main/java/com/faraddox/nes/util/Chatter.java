package com.faraddox.nes.util;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by faraddox on 27.10.2016.
 */
public class Chatter {
    public static void SEND(ICommandSender p, Object... objects) {
        String s = "";
        for (Object o : objects) s += " " + o.toString();
        p.addChatMessage(new TextComponentString(s));
    }
}
