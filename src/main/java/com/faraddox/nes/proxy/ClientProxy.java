package com.faraddox.nes.proxy;

import com.faraddox.nes.event.NESKeyEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraftforge.client.model.b3d.B3DModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by faraddox on 27.10.2016.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        ClientRegistry.registerKeyBinding(NESKeyEventHandler.openGuiKey);
        MinecraftForge.EVENT_BUS.register(new NESKeyEventHandler());

        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
