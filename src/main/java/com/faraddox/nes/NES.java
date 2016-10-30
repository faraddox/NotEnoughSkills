package com.faraddox.nes;

import com.faraddox.nes.event.NESServerEventHandler;
import com.faraddox.nes.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * Created by faraddox on 27.10.2016.
 */
@Mod(
        modid = NES.MODID,
        name = NES.MODNAME,
        version = NES.MODVERSION
)
public class NES {
    @SidedProxy(
            serverSide = "com.faraddox.nes.proxy.ServerProxy",
            clientSide = "com.faraddox.nes.proxy.ClientProxy"
    )
    public static CommonProxy proxy;

    public static final String MODID = "nes";
    public static final String MODNAME = "NorEnoughSkills";
    public static final String MODVERSION = "0.0a";
    public static SimpleNetworkWrapper skillSyncChannel;
    public static SimpleNetworkWrapper spendPointsChannel;

    @Mod.Instance(MODID)
    public static NES instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new NESCommand());
        NESServerEventHandler.serverInstance = event.getServer();
    }


}
