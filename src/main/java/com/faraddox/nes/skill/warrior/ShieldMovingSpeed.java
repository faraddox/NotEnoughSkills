package com.faraddox.nes.skill.warrior;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShield;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 01.11.2016.
 */
public final class ShieldMovingSpeed extends Skill {
    public ShieldMovingSpeed(AbstractSkillGroup _parentGroup) {
        super(_parentGroup);
    }

    public ShieldMovingSpeed(AbstractSkillGroup _parentGroup, int _currentPoints) {
        super(_parentGroup, _currentPoints);
    }

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
        return "shieldmovingspeed";
    }

    @Override
    public int getIconX() {
        return 64;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class ShieldMovingSpeedHandler {

        private static UUID attrUUID = UUID.nameUUIDFromBytes("defence".getBytes());

        @SubscribeEvent
        public void applySpeedShield(LivingEntityUseItemEvent.Start event) {
            if (event.getEntityLiving() instanceof EntityPlayer && event.getItem().getItem() instanceof ItemShield && !event.getEntityLiving().isRiding()) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                Skill skill = SkillCapability.getPlayerSkills(player).getSkillByFullName("nes:warrior.shieldmovingspeed");
                if (skill.getCurrentPoints() > 0) {
                    LOG("Adding attr for moving");
                    IAttributeInstance ias = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                    LOG("base speed: " + ias.getBaseValue());
                    LOG("cur speed: " + ias.getAttributeValue());
                    if (ias.getModifier(attrUUID) != null)
                        ias.removeModifier(attrUUID);
                    ias.applyModifier(new AttributeModifier(attrUUID, "nes:defence", (double)skill.getCurrentPoints() / 2, 1));
                    LOG("changed speed: " + ias.getAttributeValue());
                }
            }
        }

        @SubscribeEvent
        public void deleteSpeedShield(LivingEntityUseItemEvent.Stop event) {
            if (event.getItem().getItem() instanceof ItemShield && event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                LOG("Deleting attr for moving");
                IAttributeInstance ias = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                LOG("base speed: " + ias.getBaseValue());
                LOG("cur speed: " + ias.getAttributeValue());
                ias.removeModifier(attrUUID);
                LOG("changed speed: " + ias.getAttributeValue());
            }
        }

//        public void sada(PlayerInteractEvent.RightClickItem event) {
//            event.
//        }

    }
}
