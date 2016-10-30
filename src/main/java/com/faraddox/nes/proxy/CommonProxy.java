package com.faraddox.nes.proxy;

import com.faraddox.nes.NES;
import com.faraddox.nes.NESConfig;
import com.faraddox.nes.gui.NESGuiHandler;
import com.faraddox.nes.network.SkillSyncPacket;
import com.faraddox.nes.network.SpendPointsPacket;
import com.faraddox.nes.skill.farmer.FarmerSkillGroup;
import com.faraddox.nes.skill.miner.MinerSkillGroup;
import com.faraddox.nes.skill.SkillCapability;
import com.faraddox.nes.skill.farmer.FarmerExample;
import com.faraddox.nes.skill.farmer.FarmerGrowing;
import com.faraddox.nes.skill.farmer.FarmerHarvesting;
import com.faraddox.nes.skill.miner.MinerDurability;
import com.faraddox.nes.skill.miner.MinerLuck;
import com.faraddox.nes.skill.miner.MinerEfficiency;
import com.faraddox.nes.skill.warrior.WarriorAttack;
import com.faraddox.nes.skill.warrior.WarriorDefence;
import com.faraddox.nes.skill.warrior.WarriorSkillGroup;
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
       registerSkillClass(MinerSkillGroup.class, MinerLuck.class);
        reg(new MinerLuck.MinerLuckHandler());
        registerSkillClass(MinerSkillGroup.class, MinerEfficiency.class);
        reg(new MinerEfficiency.MinerEfficiencyHandler());
        registerSkillClass(MinerSkillGroup.class, MinerDurability.class);
        reg(new MinerDurability.MinerDurabilityHandler());


        //----Registering Warrior skills and event handlers
        reg(new WarriorSkillGroup.WarriorSkillGroupHandler());
        registerSkillClass(WarriorSkillGroup.class, WarriorAttack.class);
        reg(new WarriorAttack.WarriorAttackHandler());
        registerSkillClass(WarriorSkillGroup.class, WarriorDefence.class);
        reg(new WarriorDefence.WarriorDefenceHandler());

        //----Registering Stranger skills and event handlers

        //----Registering Farmer skills and event handlers
        reg(new FarmerSkillGroup.FarmerSkillGroupHandler());
        registerSkillClass(FarmerSkillGroup.class, FarmerExample.class);
        reg(new FarmerExample.FarmerExampleHandler());
        registerSkillClass(FarmerSkillGroup.class, FarmerGrowing.class);
        reg(new FarmerGrowing.FarmerGrowingHandler());
        registerSkillClass(FarmerSkillGroup.class, FarmerHarvesting.class);
        reg(new FarmerHarvesting.FarmerHarvestingHandler());

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
