package com.faraddox.nes;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.NESPlayerSkills;
import com.faraddox.nes.skill.Skill;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.lwjgl.input.Keyboard;

import java.util.*;

/**
 * Created by faraddox on 25.10.2016.
 */
public class NESConfig {
    public static NESConfig INSTANCE = new NESConfig();
    public static NESPlayerSkills nullInstance = new NESPlayerSkills(null);
    public static Configuration CLIENT_CONF;
    public static List<Property> hudProperties = new ArrayList<>();
    public static Map<String, Boolean> enabledSkillGroups = new HashMap<>();
    public static Map<String, int[]> skillGroupXpTables = new HashMap<>();
    public static Map<String, Boolean> enabledSkills = new HashMap<>();
    public static Map<String, Integer> skillCaps = new HashMap<>();
    public static Property bonusOres;
    public static Property openGuiKey;
    public static boolean showNBTinTooltips;

    public static void sync() {
        List<AbstractSkillGroup> skillGroups = nullInstance.getPlayerSkillGroups();
        //-----Client only
        //-----HUD
        CLIENT_CONF.addCustomCategoryComment("hud", "Client side only.");
        hudProperties.add(CLIENT_CONF.get("hud", "enable_hud", true, "Not implemented now").setRequiresMcRestart(true));
        hudProperties.add(CLIENT_CONF.get("hud", "hud_position", 0, "Not implemented now"));
        showNBTinTooltips = CLIENT_CONF.getBoolean("show_tooltip", "hud" , false, "show NBT-tags in item tooltips");
        //-----Keys
        CLIENT_CONF.addCustomCategoryComment("keys", "Client side only.");
        openGuiKey = CLIENT_CONF.get("keys", "open.gui.key", Keyboard.KEY_K, "Not implemented now");

        //-----Both, client and server, need sync from server
        //-----some
        CLIENT_CONF.addCustomCategoryComment("Items", "blahblahblah");

        //-----SkillsGroups
        CLIENT_CONF.addCustomCategoryComment("SkillsGroups", "Properties for groups of skills");
        for (AbstractSkillGroup sg : skillGroups) {
            String name = sg.getFullGroupName();
            enabledSkillGroups.put(name, CLIENT_CONF.get("SkillsGroups", "enable_skill_group-->" + name, true, "").getBoolean());
            skillGroupXpTables.put(name, CLIENT_CONF.get("SkillsGroups", "xp_table-->" + name,  new int[]{50, 150, 300, 500, 750, 1000}, "").getIntList());
            CLIENT_CONF.addCustomCategoryComment("SkillsGroups", "---------------------------------------------");
        }

        //-----Skills
        CLIENT_CONF.addCustomCategoryComment("Skills", "Properties for skills");
        for (AbstractSkillGroup sg : skillGroups) {
            for (Skill s : sg.getSkills()) {
                String name = s.getFullSkillName();
                enabledSkills.put(name, CLIENT_CONF.get("Skills", "enable_skill-->" + name, true, "").getBoolean());
                skillCaps.put(name, CLIENT_CONF.get("Skills", "max_level-->" + name, 5, "").getInt());
                CLIENT_CONF.addCustomCategoryComment("Skills", "---------------------------------------------");
            }
        }

        //-----Global ore list
        CLIENT_CONF.addCustomCategoryComment("Ores", "Only listed here ores will have bonuses from Miner skills");
        bonusOres = CLIENT_CONF.get("Ores", "ore_list", new String[] {
                Blocks.COAL_ORE.getRegistryName().toString(),
                Blocks.DIAMOND_ORE.getRegistryName().toString(),
                Blocks.EMERALD_ORE.getRegistryName().toString(),
                Blocks.GOLD_ORE.getRegistryName().toString(),
                Blocks.IRON_ORE.getRegistryName().toString(),
                Blocks.LAPIS_ORE.getRegistryName().toString(),
                Blocks.LIT_REDSTONE_ORE.getRegistryName().toString(),
                Blocks.QUARTZ_ORE.getRegistryName().toString(),
                Blocks.REDSTONE_ORE.getRegistryName().toString()
        }, "");
        if (CLIENT_CONF.hasChanged())
            CLIENT_CONF.save();
    }
}
