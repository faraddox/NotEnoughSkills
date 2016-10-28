package com.faraddox.nes.gui;

import com.faraddox.nes.NES;
import com.faraddox.nes.NESCommand;
import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.NESPlayerSkills;
import com.faraddox.nes.skill.SkillCapability;
import com.jcraft.jogg.Page;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.faraddox.nes.util.Logger.LOG;
import static net.minecraft.client.resources.I18n.format;

/**
 * Created by faraddox on 27.10.2016.
 */
public class NESGui extends GuiScreen {
    public ResourceLocation texture = new ResourceLocation(NES.MODID, "textures/gui/elements.png");
    public int guiX = 0;
    public int guiY = 5;
    public int guiWidth = 248;
    public int guiHeight = 0;
    public int groupX = 0;
    public int groupY = 5;
    public int groupWidth = 100;
    public int groupHeight = 20;
    public int groupScroll = 0;
    public int pageButtonHeight = 20;
    public int pageButtonWidth = 100;
    public int groupCount = 0;
    public int groupCountDisplay = 0;
    public int startingGroup = 0;
    public int groupCurrent = 0;
    public int startingGroupId = 0;
    public int skillScroll = 0;
    SkillCapability.ISkillCapability nps;
    List<AbstractSkillGroup> groupList;
    List<Integer> groupButtonIDs = new ArrayList<>();
    LanguageManager lang;
    PageButton pup;
    PageButton pdown;
    int mouseX = 0;
    int mouseY = 0;
//    private EntityPlayer player;

//    public NESGui (EntityPlayer _player) {
//        player = _player;
//        LOG("Drawing gui for " + player.getName());
//    }

    @Override
    public void initGui() {
        lang = mc.getLanguageManager();

        buttonList.clear();
        groupButtonIDs.clear();
        guiHeight = height - 60;
        guiX = (width - guiWidth)/2;
        groupX = guiX - groupWidth;
        groupY = pageButtonHeight + guiY + 3;
        nps = SkillCapability.getPlayerSkills(mc.thePlayer);
        groupList = nps.getPlayerSkillGroups();
        LOG("Player groups to display:");
        for (AbstractSkillGroup sg : groupList){
            LOG(sg.getFullGroupName());
            NESCommand.groupInfo(mc.thePlayer, sg);
        }
        groupCount = groupList.size();
        LOG("group count: " + groupCount);
        groupCountDisplay = (guiHeight - pageButtonHeight*2 - 6)/groupHeight;
        LOG("group to display1: " + groupCountDisplay);
        int id = 0;
        //ok button
        buttonList.add(new GuiButton(id++, guiX + 5, guiY + guiHeight - 25, (guiWidth - 10)/2, 20, format("gui." + NES.MODID + ".accept")));
        //cancel button
        buttonList.add(new GuiButton(id++, guiX + 5 + (guiWidth - 10)/2, guiY + guiHeight - 25, (guiWidth - 10)/2, 20, format("gui." + NES.MODID + ".cancel")));
        //page up button
        buttonList.add(pup = new PageButton(id++, groupX, guiY + 3, pageButtonWidth, pageButtonHeight, "", true));
        pup.enabled = false;
        //page down button
        buttonList.add(pdown = new PageButton(id++, groupX, guiY + guiHeight - pageButtonHeight - 6, pageButtonWidth, pageButtonHeight, "", false));
        pdown.enabled = false;
        if (groupCountDisplay > groupCount){
            groupCountDisplay = groupCount;
            pdown.enabled = false;
        }
        LOG("group to display2: " + groupCountDisplay);



        //group buttons
        startingGroupId = id;
        for (int i = 0; i < groupCountDisplay; i++) {
            int index = i + startingGroup;
            LOG("drawing group button: index = " + index + " i = " + i + " start = " + startingGroup);
            buttonList.add(new GroupButton(id++, groupX, groupY + i*groupHeight, groupWidth, groupHeight, format(groupList.get(index).getFullGroupName())));
//            buttonList.add(new GuiButton(id, groupX, groupY + i*groupHeight, groupWidth, groupHeight, format("gui.nes.farmer")));
//            groupButtonIDs.add(id++);
        }
    }

    @Override
    public void drawScreen(int _mouseX, int _mouseY, float partialTicks) {
        mouseX = _mouseX;
        mouseY = _mouseY;
        GlStateManager.color(1f, 1f, 1f, 1f);
        this.mc.getTextureManager().bindTexture(texture);
        //----draw window
        this.drawTexturedModalRect(guiX, guiY, 0, 0, guiWidth, 7);
        for (int i = 7; i < guiHeight - 7; i++) {
            this.drawTexturedModalRect(guiX, guiY + i, 0, 7, guiWidth, 1);
        }
        this.drawTexturedModalRect(guiX, guiY + guiHeight - 7, 0, 8, guiWidth, 7);
        //----draw description
        //---------------prepare string and get height
        AbstractSkillGroup sg = groupList.get(groupCurrent);
//        LOG("Drawing group: " + sg.getFullGroupName() + " index: " + groupCurrent);
        String desc = format(sg.getFullGroupName() + ".desc");
//        this.fontRendererObj.trimStringToWidth(s, guiWidth - 20);
        int descHeight = fontRendererObj.splitStringWidth(desc, guiWidth - 20) + 44;
        //---------------draw fone
        this.drawTexturedModalRect(guiX + 6, guiY + 6, 0, 15, guiWidth - 12, 4);
        for (int i = 0; i < descHeight; i++) {
            this.drawTexturedModalRect(guiX + 6, guiY + i + 10, 0, 19, guiWidth - 12, 1);
        }
        this.drawTexturedModalRect(guiX + 6, guiY + 10 + descHeight, 0, 20, guiWidth, 4);
        //---------------draw asdasdad
//        this.drawTexturedModalRect(guiX + 12, guiY + 12, 200, 24, 40, 40);
        //---------------draw icon
        ResourceLocation icons = sg.getIcons();
        this.mc.getTextureManager().bindTexture(icons);
        this.drawTexturedModalRect(guiX + 16, guiY + 16, 0, 0, 32, 32);
        this.mc.getTextureManager().bindTexture(texture);
        //---------------write level
        String level = format("gui.nes.level") + ": " + sg.getCurrentLevel() + "/" + sg.getMaxLevel();
        this.fontRendererObj.drawString(level, guiX + 52, guiY + 12, 0xdddd00, true);
        this.mc.getTextureManager().bindTexture(texture);
        //---------------draw xp line 186px
        String xp = sg.getXP() + "/" + sg.getXPForNextLevel();
        int  perc = (sg.getXP() * 100) / sg.getXPForNextLevel();
        this.drawTexturedModalRect(guiX + 52, guiY + 24, 0, 24, 186 , 5);
        this.drawTexturedModalRect(guiX + 52, guiY + 24, 0, 29, 186 * perc / 100, 5);
        this.fontRendererObj.drawStringWithShadow(xp, guiX + 135, 24, 0xdddddd);

        //---------------draw string
        this.fontRendererObj.drawSplitString(desc, guiX + 10, guiY + 52, guiWidth - 20, 0xffffff);



        //----draw skills


        //----draw buttons
        for (GuiButton b : buttonList){
            if (b instanceof GroupButton)
                ((GroupButton)b).chosen = groupCurrent == b.id - startingGroupId;
            b.drawButton(mc, mouseX, mouseY);
        }
        for (int j = 0; j < this.labelList.size(); ++j)
        {
            ((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY);
        }
    }

//    @Override
//    public void updateScreen() {
//
//    }

    public void scroll(MouseEvent event) {
        int d = event.getDwheel();
        for (GuiButton b : buttonList)
            if (b.isMouseOver())
                if (b instanceof GroupButton || b instanceof PageButton) {
                    if (groupCountDisplay > groupCount){
                        groupScroll += d;
                        if (groupScroll < 0){
                            groupScroll = 0;
                            pup.enabled = false;
                        } else {
                            pup.enabled = true;
                        }
                        int max = (groupCount - groupCountDisplay) * groupHeight;
                        if (groupScroll > max){
                            groupScroll = max;
                            pdown.enabled = false;
                        } else if (groupCount > groupCountDisplay){
                            pdown.enabled = true;
                        }
                    }
                }
        if (mouseX > guiX && mouseX < guiX + guiWidth && mouseY > guiY && mouseY < groupHeight) {
            skillScroll += d;

        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        int id = guiButton.id;
        switch (id) {
            case 0: close(); return;
            case 1: accept(); return;
        }
        if (guiButton instanceof GroupButton)
            groupCurrent = id - startingGroupId;

    }

    private void accept() {

    }

    private void close() {

    }

    private class GroupButton extends GuiButton {

        public GroupButton(int buttonId, int x, int y, String buttonText) {
            super(buttonId, x, y, buttonText);
        }

        public GroupButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
            super(buttonId, x, y, widthIn, heightIn, buttonText);
        }

        public boolean chosen;

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible)
            {
                FontRenderer fontrenderer = mc.fontRendererObj;
                mc.getTextureManager().bindTexture(texture);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                this.drawTexturedModalRect(xPosition, yPosition, 0, chosen ? 74 : 94, 100, 20);
                this.mouseDragged(mc, mouseX, mouseY);
                int j = hovered ? 16777120 : 14737632;
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
            }
        }
    }

    private class PageButton extends GuiButton {

        public PageButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean _isPageUp){
            super(buttonId, x, y, widthIn, heightIn, buttonText);
            isPageUp = _isPageUp;
        }

        public boolean isPageUp = true;

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible)
            {
                FontRenderer fontrenderer = mc.fontRendererObj;
                mc.getTextureManager().bindTexture(texture);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                if (enabled)
                    this.drawTexturedModalRect(xPosition, yPosition, 0, isPageUp ? 34 : 54, 100, 20);
                else
                    this.drawTexturedModalRect(xPosition, yPosition, 100, isPageUp ? 34 : 54, 100, 20);
                this.mouseDragged(mc, mouseX, mouseY);
                int j = hovered ? 16777120 : 14737632;
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
            }
        }
    }
}
