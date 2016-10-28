package com.faraddox.nes.event;

import com.faraddox.nes.NES;
import com.faraddox.nes.gui.NESGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by faraddox on 27.10.2016.
 */
public class NESEventHandler {
    public static NESEventHandler INSTANCE = new NESEventHandler();

    @SubscribeEvent
    public void asdsdf(BlockEvent.BreakEvent event) {
        event.getPlayer().openGui(
                NES.instance,
                NESGuiHandler.NESGUI_ID,
                Minecraft.getMinecraft().theWorld,
                0, 0, 0
        );
    }

}
