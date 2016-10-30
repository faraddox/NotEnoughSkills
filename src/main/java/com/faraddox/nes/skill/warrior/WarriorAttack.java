package com.faraddox.nes.skill.warrior;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;

/**
 * Created by faraddox on 27.10.2016.
 */
public class WarriorAttack extends Skill{
    public WarriorAttack(AbstractSkillGroup _parentGroup) {
        super(_parentGroup);
    }

    public WarriorAttack(AbstractSkillGroup _parentGroup, int _currentPoints) {
        super(_parentGroup, _currentPoints);
    }

    @Override
    public int getMaxPoints() {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getSkillName() {
        return "warriorattack";
    }

    @Override
    public int getIconX() {
        return 32;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class WarriorAttackHandler {

    }
}
