package com.faraddox.nes.skill.miner;

import com.faraddox.nes.NES;
import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public final class Durability extends Skill {

    /*Increases speed for mining Material.ROCK and ores*/
    public Durability(AbstractSkillGroup _parentGroup) {super(_parentGroup);}
    public Durability(AbstractSkillGroup _parentGroup, int _currentPoints) {super(_parentGroup, _currentPoints);}

    @Override
    public int getMaxPoints() {
        return 5;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getSkillName() {
        return "durability";
    }

    @Override
    public int getIconX() {
        return 32;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class MinerDurabilityHandler {
//
//        @SubscribeEvent
//        public void getPickaxeToSaveDurability(BlockEvent.BreakEvent event) {
//            if (Skill.checkForTool(event.getPlayer(), "pickaxe")) {
//                ItemStack pa = event.getPlayer().getHeldItemMainhand();
//                LOG("Durability: " + pa.getItemDamage());
//                NBTTagCompound nbt = pa.getTagCompound();
//                if (nbt == null)
//                    nbt = new NBTTagCompound();
//                nbt.setInteger(NES.MODID + ":durability", pa.getItemDamage());
//                pa.setTagCompound(nbt);
//            }
//        }

        @SubscribeEvent
        public void tryToSaveDurability(BlockEvent.HarvestDropsEvent event) {
            if (Skill.checkForTool(event.getHarvester(), "pickaxe")) {
                ItemStack pa = event.getHarvester().getHeldItemMainhand();
                Random r = event.getHarvester().getRNG();
                Skill s = SkillCapability.getPlayerSkills(event.getHarvester()).getSkillByFullName("nes:miner.durability");
                NBTTagCompound nbt = pa.getTagCompound();
                if (nbt == null)
                    nbt = new NBTTagCompound();
                int dur = nbt.getInteger(NES.MODID + ":durability");
                if (dur != 0) {
                    if (r.nextInt(s.getMaxPoints() + 1) < s.getCurrentPoints()) {
                        pa.setItemDamage(dur);
                        LOG("Durability saved!!!");
                    }
                }
                nbt.setInteger(NES.MODID + ":durability", pa.getItemDamage());
                pa.setTagCompound(nbt);
            }
        }
    }
}
