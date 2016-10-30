package com.faraddox.nes.skill.farmer;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.NESPlayerSkills;

/**
 * Created by faraddox on 27.10.2016.
 */
public class FarmerSkillGroup extends AbstractSkillGroup {
    public FarmerSkillGroup(NESPlayerSkills _parent) {super(_parent);}
    @Override
    public String getGroupName() {return "farmer";}
    @Override
    public boolean isEnabled() {return true;}

    public static class FarmerSkillGroupHandler {

    }
}
