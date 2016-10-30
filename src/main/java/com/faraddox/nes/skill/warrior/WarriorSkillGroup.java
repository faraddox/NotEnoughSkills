package com.faraddox.nes.skill.warrior;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.NESPlayerSkills;

/**
 * Created by faraddox on 27.10.2016.
 */
public class WarriorSkillGroup extends AbstractSkillGroup {
    public WarriorSkillGroup(NESPlayerSkills _parent) {
        super(_parent);
    }

    @Override
    public String getGroupName() {
        return "warrior";
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    public static class WarriorSkillGroupHandler {

    }
}
