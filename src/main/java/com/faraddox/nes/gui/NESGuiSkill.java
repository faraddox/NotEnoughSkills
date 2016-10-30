package com.faraddox.nes.gui;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.faraddox.nes.util.Logger.LOG;
import static net.minecraft.client.resources.I18n.format;

/**
 * Created by faraddox on 29.10.2016.
 */
public class NESGuiSkill extends NESGuiScroll {
    protected List<AbstractSkillGroup> skillGroupList;
    protected NESGui parent;
    protected Map<Skill, SkillButton> plusButtons = new HashMap<>();
    protected Map<Skill, SkillButton> minusButtons = new HashMap<>();
    protected Map<Skill, Integer> spendedPoints = new HashMap<>();
    private Map<AbstractSkillGroup, Integer> canSpendPoints = new HashMap<>();

    public NESGuiSkill(NESGui _parent, List<AbstractSkillGroup> _skillGroupList) {
        super(_parent,
                _parent.sWidth,    // width,
                _parent.sHeight,   // height,
                _parent.sTop,      // top,
                _parent.sBottom,   // bottom,
                _parent.sLeft,     // left,
//                40,                 // entryHeight,
                _parent.width,      // screenWidth,
                _parent.height);    // screenHeight
        parent = _parent;
        skillGroupList = _skillGroupList;
        reset();
    }

    @Override
    protected int getSize() {
        return skillGroupList.get(parent.groupCurrent).getSkills().size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        LOG("Pressed at index " + index);
        if (getCurrentGroup() > -1 && getCurrentGroup() < skillGroupList.size()) {
            AbstractSkillGroup sg = skillGroupList.get(getCurrentGroup());
            Skill skill = sg.getSkills().get(index);
            SkillButton bPlus = plusButtons.get(skill);
            SkillButton bMinus = minusButtons.get(skill);
            if (bPlus.mousePressed(client, mouseX, mouseY)) {
                LOG("Pressed + for " + skill.getFullSkillName());
                if (canSpendPoints.get(sg) > 0 && bPlus.enabled && spendedPoints.get(skill) < skill.getMaxPoints()) {
                    LOG("SPENDING POINTS");
                    int i = canSpendPoints.get(sg);
                    canSpendPoints.put(sg, --i);
                    int j = spendedPoints.get(skill);
                    spendedPoints.put(skill, ++j);
                }
            } else if (bMinus.mousePressed(client, mouseX, mouseY)) {
                LOG("Pressed - for " + skill.getFullSkillName());
                if (bMinus.enabled && spendedPoints.get(skill) > 0) {
                    LOG("RETURNING POINTS");
                    int i = canSpendPoints.get(sg);
                    canSpendPoints.put(sg, ++i);
                    int j = spendedPoints.get(skill);
                    spendedPoints.put(skill, --j);
                }
            }
        }
    }

    @Override
    protected void drawBackground() {
        this.setHeaderInfo(true, getHeaderHeight());
        GlStateManager.color(1f, 1f, 1f, 1f);
        parent.mc.getTextureManager().bindTexture(parent.elements);
        //----draw head
        parent.drawTexturedModalRect(left, top, 252, 15, 4, 7);
        parent.drawTexturedModalRect(left + 4, top, 260 - listWidth, 0, listWidth - 4, 7);
        parent.drawTexturedModalRect(right - 13, top, 243, 15, 9, 7);

        //draw body
        for (int i = 7; i < bottom - top - 7; i++) {
            parent.drawTexturedModalRect(left, top + i, 252, 22, 4, 1);
            parent.drawTexturedModalRect(left + 4, top + i, 260 - listWidth, 7, listWidth - 4, 1);
            parent.drawTexturedModalRect(right - 13, top + i, 243, 22, 9, 1);
        }

        //draw bottom
        parent.drawTexturedModalRect(left, bottom - 7, 252, 23, 4, 7);
        parent.drawTexturedModalRect(left + 4, bottom - 7, 260 - listWidth, 8, listWidth - 4, 7);
        parent.drawTexturedModalRect(right - 13, bottom - 7, 243, 23, 9, 7);

    }

    @Override
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
        int index = getCurrentGroup();
        if (index > -1 && index < skillGroupList.size()) {
//            LOG("HEADER HEIGHT: " + getHeaderHeight());
            AbstractSkillGroup skillGroup = skillGroupList.get(index);
            int hWidth = entryRight - left;
//            LOG("HEADER WIDTH: " + hWidth);
            //---------------write level
            String level = format("gui.nes.level") + ": " + skillGroup.getCurrentLevel() + "/" + skillGroup.getMaxLevel();
            parent.mc.fontRendererObj.drawString(level, left + 5, relativeY + 5, 0xdddd00, true);
            String points = format("gui.nes.freepoints") + ": " + canSpendPoints.get(skillGroup);
            parent.mc.fontRendererObj.drawString(points, left + 5, relativeY + 15, 0xdddd00, true);
            parent.mc.getTextureManager().bindTexture(parent.elements);
            //---------------draw xp line 186px
            String xp = skillGroup.getXP() + "/" + skillGroup.getXPForNextLevel();
            int  perc = (skillGroup.getXP() * 100) / skillGroup.getXPForNextLevel();
            if (perc > 100) perc = 100;
            GlStateManager.color(1f, 1f, 1f, 1f);
            parent.drawTexturedModalRect(left + 5, relativeY + 32, 0, 34, 186 , 10);
            parent.drawTexturedModalRect(left + 5, relativeY + 32, 0, 24, 186 * perc / 100, 10);
            parent.mc.fontRendererObj.drawStringWithShadow(xp, left + 93, relativeY + 32, 0xffffff);
            //---------------draw string
            String desc = format(skillGroup.getFullGroupName() + ".desc");
            parent.mc.fontRendererObj.drawSplitString(desc, left + 5, relativeY + 52, hWidth - 5, 0xffffff);
//            LOG(desc);
        }
    }

    @Override
    protected int setSelectedIndex(int mouseListY) {
        if (getCurrentGroup() > -1 && getCurrentGroup() < skillGroupList.size()){
            AbstractSkillGroup sg = skillGroupList.get(getCurrentGroup());
            int size = 0;
            for (int i = 0; i < sg.getSkills().size(); i ++) {
                size += getEntryHeight(sg.getSkills().get(i));
                if (size > mouseListY) return i;
            }
        }
        return -1;
//        int mouseListY = mouseY - this.top - this.headerHeight + (int)this.scrollDistance - border;
//        mouseListY / this.slotHeight
    }

    @Override
    protected int getAllHeightForEntry(int slotIdx) {
        int size = 0;
        for (int i = 0; i < slotIdx; i++) {
            size += getEntryHeight(i);
        }
        return size;
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        int index = getCurrentGroup();
        if (index > -1 && index < skillGroupList.size()){
            AbstractSkillGroup skillGroup = skillGroupList.get(index);
            if (slotIdx > -1 && slotIdx < skillGroup.getSkills().size()){
                Skill skill = skillGroup.getSkills().get(slotIdx);
                int entryWidth = entryRight - left - 3;
//                LOG("Slot width: " + entryWidth);
                parent.mc.getTextureManager().bindTexture(parent.elements);
                //-------------draw fone
                parent.drawTexturedModalRect(left + 3, slotTop, 0, 15, 235, 4);
                int size = getEntryHeight(skill);
                for (int i = 4; i < size - 4; i++) {
                    parent.drawTexturedModalRect(left + 3, slotTop + i, 0, 19, 235, 1);
                }
                parent.drawTexturedModalRect(left + 3, slotTop + size - 4, 0, 20, 235, 4);
                //-------------draw icon
                parent.mc.getTextureManager().bindTexture(skillGroup.getIcons());
//        parent.drawTexturedModalRect(left + 9, slotTop + 2, skill.getIconX(), skill.getIconY(), 16, 16);
                parent.drawTexturedModalRect(left + 5, slotTop + 2, 0, 0, 16, 16);
                //-------------draw name
                parent.mc.fontRendererObj.drawString(format(skill.getFullSkillName()), left + 23, slotTop + 5, 0xffffff, true);
                //-------------draw desc
                String desc = format(skill.getFullSkillName() + ".desc");
                parent.mc.fontRendererObj.drawSplitString(desc, left + 10, slotTop + 20, 233, 0xffffff);
                //-------------draw points
                int points = skill.getCurrentPoints() + spendedPoints.get(skill);
                parent.mc.fontRendererObj.drawString("" + points + "/" + skill.getMaxPoints(), entryRight - 50, slotTop + 5, 0xffffff, true);
                //-------------draw buttons
                SkillButton bPlus = plusButtons.get(skill);
                SkillButton bMinus = minusButtons.get(skill);
                bPlus.enabled = (canSpendPoints.get(skillGroup) > 0);
                bMinus.enabled = (spendedPoints.get(skill) > 0);
                bPlus.drawButton(entryRight - 35, slotTop + 5);
                bMinus.drawButton(entryRight - 75, slotTop + 5);

            }
        }
    }

    @Override
    protected int getContentHeight()
    {
        if (getCurrentGroup() > -1 && getCurrentGroup() < skillGroupList.size()) {
            AbstractSkillGroup sg = skillGroupList.get(getCurrentGroup());
            if (sg == null) return 0;
            int size = 5;
            for (Skill skill : sg.getSkills()) {
                size += getEntryHeight(skill);
            }
            return size + this.headerHeight;
        }
        return 0;
    }

    protected int getEntryHeight(Skill skill) {
        return parent.mc.fontRendererObj.splitStringWidth(format(skill.getFullSkillName() + ".desc"), listWidth - 30) + 32;
    }

    @Override
    protected int getEntryHeight(int index) {
        if (getCurrentGroup() > -1 && getCurrentGroup() < skillGroupList.size()) {
            AbstractSkillGroup sg = skillGroupList.get(getCurrentGroup());
            if (sg == null) return 0;
            return parent.mc.fontRendererObj.splitStringWidth(format(sg.getSkills().get(index).getFullSkillName() + ".desc"), listWidth - 30) + 32;
        }
        return 0;
    }

    protected int getHeaderHeight() {
        int size = 0;
        if (getCurrentGroup() > -1 && getCurrentGroup() < skillGroupList.size())
            size = parent.mc.fontRendererObj.splitStringWidth(format(skillGroupList.get(getCurrentGroup()).getFullGroupName() + ".desc"), listWidth - 30) + 55;
        return size;
    }

    protected int getCurrentGroup() {
        return parent.nesGuiSkillGroup.selectedIndex;
    }

    public void reset() {
        for (AbstractSkillGroup sg : skillGroupList){
            canSpendPoints.put(sg, sg.getFreePoints());
            for (Skill s : sg.getSkills()) {
                plusButtons.put(s, new SkillButton(s, true));
                minusButtons.put(s, new SkillButton(s, false));
                spendedPoints.put(s, 0);
            }
        }
    }

    private class SkillButton extends GuiButton {
        public Skill skill;
        public boolean isPlus;

        public SkillButton(Skill _skill, boolean _isPlus) {
            super(0, 0, 0, 13, 13, "");
            skill = _skill;
            isPlus = _isPlus;
        }

        public void drawButton(int x, int y) {
            this.xPosition = x;
            this.yPosition = y;
            drawButton(client, mouseX, mouseY);
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY)
        {
            if (this.visible)
            {
                FontRenderer fontrenderer = mc.fontRendererObj;
                mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int i = this.getHoverState(this.hovered);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//                this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
//                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
                mc.getTextureManager().bindTexture(parent.elements);
                int x = (this.hovered || mousePressed(mc, mouseX, mouseY)) ? 243 : 230;
                int y = (!this.enabled || this.hovered) ? 43 : 30;
                this.drawTexturedModalRect(xPosition, yPosition, x, y, width, height);
                x = (this.enabled) ? 221 : 212;
                y = (this.isPlus) ? 24 : 33;
                this.drawTexturedModalRect(xPosition + 2, yPosition + 2, x, y, 9, 9);

                this.mouseDragged(mc, mouseX, mouseY);
            }
        }
    }


}
