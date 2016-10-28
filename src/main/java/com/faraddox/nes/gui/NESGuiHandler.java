package com.faraddox.nes.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by faraddox on 27.10.2016.
 */
public class NESGuiHandler implements IGuiHandler {

    public static final int NESGUI_ID = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case NESGUI_ID:
                return new NESGui();
        }
        return null;
    }
}
