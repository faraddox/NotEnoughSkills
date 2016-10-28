package com.faraddox.nes.skill;

import com.faraddox.nes.NES;
import com.faraddox.nes.util.NESCapabilityUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public class SkillCapability {

    @CapabilityInject(ISkillCapability.class)
    public static final Capability<ISkillCapability> SKILL_CAPABILITY = null;
    public static final EnumFacing DEFAULT_FACING = null;
    public static final ResourceLocation ID = new ResourceLocation(NES.MODID, "nesskills");

    public static void register() {
        LOG("Registrating capability");
        CapabilityManager.INSTANCE.register(ISkillCapability.class, new Capability.IStorage<ISkillCapability>() {

            @Override
            public NBTBase writeNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side) {
                return instance.saveToNBT();
            }

            @Override
            public void readNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side, NBTBase nbt) {
                instance.loadFromNBT((NBTTagCompound)nbt);
            }
        }, () -> new NESPlayerSkills(null));
        LOG("Registration completed");
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        LOG("Capability event handler installed");
    }

    public static ISkillCapability getPlayerSkills (EntityPlayer entity) {
        return NESCapabilityUtils.getCapability(entity, SKILL_CAPABILITY, DEFAULT_FACING);
    }

    public static ICapabilityProvider createProvider(ISkillCapability skills) {
        return new SkillCapabilityProvider<>(SKILL_CAPABILITY, DEFAULT_FACING, skills);
    }

    public static class EventHandler {
        @SubscribeEvent
        public void attachCapabilities(AttachCapabilitiesEvent.Entity event) {
            if (event.getEntity() instanceof EntityPlayer) {
                final NESPlayerSkills skills = new NESPlayerSkills((EntityPlayer)event.getEntity());
                event.addCapability(ID, createProvider(skills));
                LOG("Capability added");
            }
        }

        @SubscribeEvent
        public void playerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
            LOG("Cloning capability");
            final ISkillCapability oldSkills = getPlayerSkills(event.getOriginal());
            final ISkillCapability newSkills = getPlayerSkills(event.getEntityPlayer());
            if (newSkills != null && oldSkills != null) {
                newSkills.setPlayerSkillGroups(oldSkills.getPlayerSkillGroups());
                LOG("Cloned!");
            }
        }
    }


    public interface ISkillCapability {

        List<AbstractSkillGroup> getPlayerSkillGroups();
        AbstractSkillGroup getSkillGroupByFullName(String name);
        Skill getSkillByFullName(String name);
        void setPlayerSkillGroups(List<AbstractSkillGroup> skills);
        NBTTagCompound saveToNBT();
        void loadFromNBT(NBTTagCompound nbt);
    }

}
