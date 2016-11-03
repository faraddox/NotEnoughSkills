package com.faraddox.nes.gui;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiClickableScrolledSelectionListProxy;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.List;
import static net.minecraft.client.resources.I18n.format;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 29.10.2016.
 */
public class NESGuiSkillGroup extends NESGuiScroll {
    private List<AbstractSkillGroup> skillGroupList;
    private NESGui parent;
    private int slotHeight;

    public NESGuiSkillGroup(NESGui _parent, List<AbstractSkillGroup> _skillGroupList) {
        super(_parent,
                _parent.sgWidth,    // width,
                _parent.sgHeight,   // height,
                _parent.sgTop,      // top,
                _parent.sgBottom,   // bottom,
                _parent.sgLeft,     // left,
//                20,                 // entryHeight,
                _parent.width,      // screenWidth,
                _parent.height);    // screenHeight
        parent = _parent;
        skillGroupList = _skillGroupList;
        slotHeight = 36;
    }

    @Override
    protected int getContentHeight()
    {
        return this.getSize() * this.slotHeight + this.headerHeight;
    }

    @Override
    protected int getSize() {
        return skillGroupList.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        parent.groupCurrent = index;
    }

    @Override
    protected void drawBackground() {
        GlStateManager.color(1f, 1f, 1f, 1f);
        parent.mc.getTextureManager().bindTexture(parent.elements);
        //----draw head
        parent.drawTexturedModalRect(left, top, 0, 0, listWidth - 8, 7);
        parent.drawTexturedModalRect(right - 9, top, 243, 15, 9, 7);
        //draw body
        for (int i = 7; i < bottom - top - 7; i++) {
            parent.drawTexturedModalRect(left, top + i, 0, 7, listWidth - 8, 1);
            parent.drawTexturedModalRect(right - 9, top + i, 243, 22, 9, 1);
        }

        //draw bottom
        parent.drawTexturedModalRect(left, bottom - 7, 0, 8, listWidth - 8, 7);
        parent.drawTexturedModalRect(right - 9, bottom - 7, 243, 23, 9, 7);
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        if (slotIdx > -1 && slotIdx < skillGroupList.size())
        {
            AbstractSkillGroup sg = skillGroupList.get(slotIdx);
            if (selectedIndex == slotIdx && !sg.isEnabled())
                selectedIndex = -1;
            int entryWidth = entryRight - left - 7;
//        LOG("Slot width: " + entryWidth); 86
            parent.mc.getTextureManager().bindTexture(parent.elements);
            int posY = (selectedIndex == slotIdx) ? 84 : 104;
            if (!sg.isEnabled()) posY = 124;
            parent.drawTexturedModalRect(left + 11, slotTop, 0, posY, entryWidth, 3);
            for (int i = 3; i < 34; i++)
                parent.drawTexturedModalRect(left + 11, slotTop + i, 0, posY + 3, entryWidth, 1);
            parent.drawTexturedModalRect(left + 11, slotTop + 34, 0, posY + 17, entryWidth, 3);
            parent.mc.getTextureManager().bindTexture(sg.getIcons());
            parent.drawTexturedModalRect(left + 13, slotTop + 2, 0, 0, 32, 32);
            parent.mc.fontRendererObj.drawString(format(sg.getFullGroupName()), left + 47, slotTop + 15, 0xffffff, true);
        }
    }

    @Override
    protected int setSelectedIndex(int mouseListY) {
        return mouseListY / this.slotHeight;
    }

    @Override
    protected int getAllHeightForEntry(int slotIdx) {
        return slotIdx * this.slotHeight;
    }

    @Override
    protected int getEntryHeight(int index) {
        return slotHeight;
    }

//    @Override
//    protected int getContentHeight()
//    {
//        int size = 0;
//        for (AbstractSkillGroup sg : skillGroupList) {
//            size += parent.mc.fontRendererObj.splitStringWidth(format(skill.getFullSkillName() + ".desc"), listWidth - 30);
//        }
//        size += 32;
//        return size + this.headerHeight;
//    }

//    protected int cont
}
