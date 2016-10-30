package com.faraddox.nes.event;

import com.faraddox.nes.NES;
import com.faraddox.nes.gui.NESGui;
import com.faraddox.nes.gui.NESGuiHandler;
import com.faraddox.nes.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public class NESClientEventHandler extends NESCommonEventHandler{

    public static KeyBinding openGuiKey = new KeyBinding("nes.openGuiKey", Keyboard.KEY_K, "nes.gui");

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(InputEvent event) {
        if (openGuiKey.isPressed()) {
//            Minecraft.getMinecraft().displayGuiScreen(new NESGui());
            Minecraft.getMinecraft().thePlayer.openGui(
                    NES.instance,
                    NESGuiHandler.NESGUI_ID,
                    Minecraft.getMinecraft().theWorld,
                    0, 0, 0
            );
        }

    }
}
