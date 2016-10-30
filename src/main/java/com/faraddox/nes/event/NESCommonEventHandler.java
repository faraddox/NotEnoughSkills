package com.faraddox.nes.event;

import com.faraddox.nes.NES;
import com.faraddox.nes.gui.NESGuiHandler;
import com.faraddox.nes.skill.NESPlayerSkills;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public class NESCommonEventHandler {

    @SubscribeEvent
    public void onClientConnected(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.getEntityWorld().isRemote)
            NESPlayerSkills.syncSkills();
    }

}
