package com.faraddox.nes.skill.warrior;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 01.11.2016.
 */
public final class JumpAttack extends Skill {
    public JumpAttack(AbstractSkillGroup _parentGroup) {
        super(_parentGroup);
    }

    public JumpAttack(AbstractSkillGroup _parentGroup, int _currentPoints) {
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
        return "jumpattack";
    }

    @Override
    public int getIconX() {
        return 32;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class JumpAttackHandler {

        @SubscribeEvent
        public void jumpAttack(LivingHurtEvent event) {
            if (event.getSource().getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)event.getSource().getEntity();
                Skill skill = SkillCapability.getPlayerSkills(player).getSkillByFullName("nes:warrior.jumpattack");
                double mot = player.motionY;
                if (mot > 0 && skill.getCurrentPoints() > 0) {
                    LOG("Speed Y: " + mot);
                    LOG("Orig attack " + event.getAmount());
                    float bonus = event.getAmount() + (float)(skill.getCurrentPoints() * 15 * mot);
                    LOG("New attack Y " + bonus);
                    event.setAmount(bonus);
                    player.fallDistance = 0;
                }
            }
        }

    }
}
