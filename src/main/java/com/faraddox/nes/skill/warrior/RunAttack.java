package com.faraddox.nes.skill.warrior;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public final class RunAttack extends Skill{
    public RunAttack(AbstractSkillGroup _parentGroup) {
        super(_parentGroup);
    }

    public RunAttack(AbstractSkillGroup _parentGroup, int _currentPoints) {
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
        return "runattack";
    }

    @Override
    public int getIconX() {
        return 128;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class RunAttackHandler {

        @SubscribeEvent
        public void onAttack(LivingHurtEvent event) {
            if (event.getSource().getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)event.getSource().getEntity();
                Skill s = SkillCapability.getPlayerSkills(player).getSkillByFullName("nes:warrior.runattack");
                double speed = Math.sqrt(Math.pow(player.prevChasingPosX - player.chasingPosX, 2) + Math.pow(player.prevChasingPosZ - player.chasingPosZ, 2));
                LOG("Speed: " + speed);
                LOG("Orig attack " + event.getAmount());
                float bonus = event.getAmount() + (float)(s.getCurrentPoints() * 5 * speed);
                LOG("New attack " + bonus);
                event.setAmount(bonus);
            }
        }
    }
}
