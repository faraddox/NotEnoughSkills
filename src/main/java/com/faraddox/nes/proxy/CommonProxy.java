package com.faraddox.nes.proxy;

import com.faraddox.nes.NES;
import com.faraddox.nes.NESConfig;
import com.faraddox.nes.gui.NESGuiHandler;
import com.faraddox.nes.network.SkillSyncPacket;
import com.faraddox.nes.network.SpendPointsPacket;
import com.faraddox.nes.skill.farmer.FarmerSkillGroup;
import com.faraddox.nes.skill.farmer.HoeRadius;
import com.faraddox.nes.skill.miner.*;
import com.faraddox.nes.skill.SkillCapability;
import com.faraddox.nes.skill.farmer.Growing;
import com.faraddox.nes.skill.farmer.Harvesting;
import com.faraddox.nes.skill.miner.Durability;
import com.faraddox.nes.skill.warrior.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import static com.faraddox.nes.util.Logger.LOG;
import static com.faraddox.nes.skill.NESPlayerSkills.registerSkillClass;

/**
 * Created by faraddox on 27.10.2016.
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        NES.skillSyncChannel = NetworkRegistry.INSTANCE.newSimpleChannel("SkillSyncChannel");
        NES.skillSyncChannel.registerMessage(SkillSyncPacket.SkillSyncHandler.class, SkillSyncPacket.class, 0, Side.CLIENT);
        NES.spendPointsChannel = NetworkRegistry.INSTANCE.newSimpleChannel("SpendPointsChannel");
        NES.spendPointsChannel.registerMessage(SpendPointsPacket.SpendPointsHandler.class, SpendPointsPacket.class, 1, Side.SERVER);
        NESConfig.CLIENT_CONF = new Configuration(event.getSuggestedConfigurationFile());
        LOG("PreInit complete");
    }

    public void init(FMLInitializationEvent event) {
        SkillCapability.register();
        //----Registering Miner skills and event handlers
        reg(new MinerSkillGroup.MinerSkillGroupHandler());
       registerSkillClass(MinerSkillGroup.class, Luck.class);
        reg(new Luck.MinerLuckHandler());
        registerSkillClass(MinerSkillGroup.class, Efficiency.class);
        reg(new Efficiency.MinerEfficiencyHandler());
        registerSkillClass(MinerSkillGroup.class, Durability.class);
        reg(new Durability.MinerDurabilityHandler());
        registerSkillClass(MinerSkillGroup.class, NightVision.class);
        reg(new NightVision.MinerNightVisionHandler());


        //----Registering Warrior skills and event handlers
        reg(new WarriorSkillGroup.WarriorSkillGroupHandler());
        registerSkillClass(WarriorSkillGroup.class, RunAttack.class);
        reg(new RunAttack.RunAttackHandler());
        registerSkillClass(WarriorSkillGroup.class, SneakKnockback.class);
        reg(new SneakKnockback.SneakKnockbackHandler());
        registerSkillClass(WarriorSkillGroup.class, JumpAttack.class);
        reg(new JumpAttack.JumpAttackHandler());
        registerSkillClass(WarriorSkillGroup.class, ShieldMovingSpeed.class);
        reg(new ShieldMovingSpeed.ShieldMovingSpeedHandler());


        //----Registering Stranger skills and event handlers

        //----Registering Farmer skills and event handlers
        reg(new FarmerSkillGroup.FarmerSkillGroupHandler());
        registerSkillClass(FarmerSkillGroup.class, HoeRadius.class);
        reg(new HoeRadius.HoeRadiusHandler());
        registerSkillClass(FarmerSkillGroup.class, Growing.class);
        reg(new Growing.GrowingHandler());
        registerSkillClass(FarmerSkillGroup.class, Harvesting.class);
        reg(new Harvesting.HarvestingHandler());

        //----Registering Craftsman skills and event handlers



        NESConfig.sync();
        LOG("Init complete");
    }

    public void postInit(FMLPostInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(NES.instance, new NESGuiHandler());

        LOG("PostInit complete");
    }

    public void reg(Object o) {
        MinecraftForge.EVENT_BUS.register(o);
    }
}
