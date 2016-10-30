package com.faraddox.nes.skill;

import com.faraddox.nes.NES;
import com.faraddox.nes.event.NESServerEventHandler;
import com.faraddox.nes.network.SkillSyncPacket;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public class NESPlayerSkills implements SkillCapability.ISkillCapability {

    private static Multimap<Class<? extends AbstractSkillGroup>, Class<? extends Skill>> _skillSystem = ArrayListMultimap.create();
    public static Map<String, Integer> skillIds = new HashMap<>();
    private static int index = 0;

    private final EntityPlayer player;
    private List<AbstractSkillGroup> skillGroups = new ArrayList<>();

    public NESPlayerSkills(@Nullable EntityPlayer _player) {
        player = _player;
        String name = "nullProfile";
        if (player != null && player.getGameProfile() != null) name = player.getName();
        LOG("Creating skills for " + name);
        _skillSystem.keySet().forEach((Class<? extends AbstractSkillGroup> sgc) -> {
            try {
                AbstractSkillGroup sg = sgc.getDeclaredConstructor(this.getClass()).newInstance(this);
                LOG("Created skill group " + sg.getFullGroupName());
                _skillSystem.get(sgc).forEach((Class<? extends Skill> sc) -> {
                    try {
                        Skill s = sc.getDeclaredConstructor(AbstractSkillGroup.class).newInstance(sg);
                        LOG("Created skill " + s.getFullSkillName());
                        sg.addSkill(s);
                        LOG("Skill " + s.getFullSkillName() + " added into group " + sg.getFullGroupName());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
                skillGroups.add(sg);
                LOG("Group " + sg.getFullGroupName() + " added");
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        skillGroups.sort((AbstractSkillGroup sg1, AbstractSkillGroup sg2) -> sg1.getGroupName().compareTo(sg2.getGroupName()));
        LOG("All skills for " + name + " created!");
    }

    public static void registerSkillClass(Class<? extends AbstractSkillGroup> sgc, Class<? extends Skill> sc) {
        _skillSystem.put(sgc, sc);
    }

    public static void syncSkills() {
        LOG("Sync skills");
//        EntityPlayerMP entityPlayerMP = NESServerEventHandler.serverInstance.getPlayerList().getPlayerByUUID(player.getUniqueID());
//        LOG("Player: " + (entityPlayerMP == null ? "null" : entityPlayerMP.getCachedUniqueIdString()));
//        NES.skillSyncChannel.sendTo(new SkillSyncPacket(this.skillGroups), entityPlayerMP);
        for (EntityPlayerMP entityPlayerMP : NESServerEventHandler.serverInstance.getPlayerList().getPlayerList()) {
            LOG("Sync " + SkillCapability.getPlayerSkills(entityPlayerMP).getPlayerSkillGroups().size() + " groups");
            if (NES.skillSyncChannel == null) LOG("null channel");
            NES.skillSyncChannel.sendTo(new SkillSyncPacket(SkillCapability.getPlayerSkills(entityPlayerMP).getPlayerSkillGroups()), entityPlayerMP);
        }


    }

    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public final List<AbstractSkillGroup> getPlayerSkillGroups() {
        return skillGroups;
    }

    @Override
    public AbstractSkillGroup getSkillGroupByFullName(String name) {
        for (AbstractSkillGroup sg : skillGroups)
            if (sg.getFullGroupName().equals(name))
                return sg;
        return null;
    }

    @Override
    public Skill getSkillByFullName(String name) {
        return getSkillGroupByFullName(name.split("\\.")[0]).getSkillByFullName(name);
    }

    @Override
    public final void setPlayerSkillGroups(List<AbstractSkillGroup> _skillGroups) {
        skillGroups = _skillGroups;
    }

    @Override
    public NBTTagCompound saveToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        skillGroups.forEach((AbstractSkillGroup sg) -> sg.saveToNBT(nbt));
        LOG("NBT saving: " + nbt);
        NBTTagCompound result = new NBTTagCompound();
        result.setTag(player.getCachedUniqueIdString(), nbt);
        return result;
    }

    @Override
    public void loadFromNBT(NBTTagCompound nbt) {
        LOG("NBT loading: " + nbt);
        NBTTagCompound toGetFrom = nbt.getCompoundTag(player.getCachedUniqueIdString());
        LOG(player.getCachedUniqueIdString());
        //faraddox 29e30536-8467-330d-a144-5b1d0b32dbbb
        skillGroups.forEach((AbstractSkillGroup sg) -> sg.loadFromNBT(toGetFrom));
        if (!player.getEntityWorld().isRemote)
            syncSkills();
    }

    @Override
    public void trySpendPoints(Map<String, Integer> spendedPoints) {
        LOG("Starting to spend points");
        for (AbstractSkillGroup sg : skillGroups) {
            LOG("Spending points in " + sg.getFullGroupName());
            for (String s : spendedPoints.keySet()) {
                if (s.startsWith(sg.getFullGroupName())) {
                    Skill skill = sg.getSkillByFullName(s);
                    int points = spendedPoints.get(s);
                    LOG("Trying to add " + points + " points to " + skill.getFullSkillName());
                    if (sg.getFreePoints() >= points)
                        LOG("Enough free points");
                        if (skill.addLevel(points)) {
                            LOG("Adding point to " + skill.getFullSkillName());
                            sg.decFreePoints(points);
                        }
                }
            }
        }
        syncSkills();
    }

    public static boolean checkForCapability(Entity entity){
        return  (entity.hasCapability(SkillCapability.SKILL_CAPABILITY, SkillCapability.DEFAULT_FACING));
    }

}
