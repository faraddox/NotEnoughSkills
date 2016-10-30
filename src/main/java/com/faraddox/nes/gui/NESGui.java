package com.faraddox.nes.gui;

import com.faraddox.nes.NES;
import com.faraddox.nes.NESCommand;
import com.faraddox.nes.network.SpendPointsPacket;
import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.faraddox.nes.util.Logger.LOG;
import static net.minecraft.client.resources.I18n.format;

/**
 * Created by faraddox on 27.10.2016.
 */
public class NESGui extends GuiScreen {
    public ResourceLocation elements = new ResourceLocation(NES.MODID, "textures/gui/elements.png");
    public NESGuiSkillGroup nesGuiSkillGroup;
    public NESGuiSkill nesGuiSkill;
    public int sgLeft = 0;
    public int sgTop = 0;
    public int sgRight = 0;
    public int sgBottom = 0;
    public int sgWidth = 0;
    public int sgHeight = 0;
    public int sLeft = 0;
    public int sTop = 0;
    public int sRight = 0;
    public int sBottom = 0;
    public int sWidth = 0;
    public int sHeight = 0;
    public int groupCurrent = 0;
    public int buttonWidth = 70;
    public int buttonHeight = 13;
    SkillCapability.ISkillCapability nps;
    List<AbstractSkillGroup> groupList;
//    LanguageManager lang;

    @Override
    public void initGui() { //800x600
//        lang = mc.getLanguageManager();
        sgWidth = 100;
        sWidth = 253;
        buttonList.clear();
        sgTop = sTop = 5;
        sgBottom = sBottom = height - 60;
        sgHeight = sHeight = sgTop - sgBottom;
        sgLeft = (width - sgWidth - sWidth) / 2;
        sLeft = sgRight = sgLeft + sgWidth;
        sRight = sLeft + sWidth;
        nps = SkillCapability.getPlayerSkills(mc.thePlayer);
        groupList = nps.getPlayerSkillGroups();
        LOG("Player groups to display:");
        for (AbstractSkillGroup sg : groupList){
            LOG(sg.getFullGroupName());
            NESCommand.groupInfo(mc.thePlayer, sg);
        }
        nesGuiSkillGroup = new NESGuiSkillGroup(this, groupList);
        nesGuiSkill = new NESGuiSkill(this, groupList);

        //spend button
        buttonList.add(new AcceptReset(0, width / 2 - buttonWidth - 3, sBottom + 6, buttonWidth, buttonHeight, format("gui." + NES.MODID + ".spend"), true));
        //reset button
        buttonList.add(new AcceptReset(1, width / 2 + 3, sBottom + 6, buttonWidth, buttonHeight, format("gui." + NES.MODID + ".reset"), false));

    }

    @Override
    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        super.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        nesGuiSkillGroup.drawScreen(mouseX, mouseY, partialTicks);
        nesGuiSkill.drawScreen(mouseX, mouseY, partialTicks);
        //----draw buttons
        for (GuiButton b : buttonList){
            b.drawButton(mc, mouseX, mouseY);
        }
//        for (int j = 0; j < this.labelList.size(); ++j)
//        {
//            ((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY);
//        }
    }

//    @Override
//    public void updateScreen() {
//        drawScreen(Mouse.getX(), Mouse.getY(), );
//    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        int id = guiButton.id;
        switch (id) {
            case 0: spend(); return;
            case 1: reset(); return;
        }

    }

    private void spend() {
        Map<String, Integer> spendedPoints = new HashMap<>();
        for (Skill s : nesGuiSkill.spendedPoints.keySet())
            if (nesGuiSkill.spendedPoints.get(s) > 0)
                spendedPoints.put(s.getFullSkillName(), nesGuiSkill.spendedPoints.get(s));
        NES.spendPointsChannel.sendToServer(new SpendPointsPacket(spendedPoints));
        SkillCapability.getPlayerSkills(mc.thePlayer).trySpendPoints(spendedPoints);
        reset();
    }

    private void reset() {
        nesGuiSkill.reset();
    }

    private class AcceptReset extends GuiButton {

        public AcceptReset(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean _isAccept) {
            super(buttonId, x, y, widthIn, heightIn, buttonText);
            isAccept = _isAccept;
        }

        public boolean isAccept;

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible)
            {
                FontRenderer fontrenderer = mc.fontRendererObj;
                mc.getTextureManager().bindTexture(elements);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                int x = (this.hovered || mousePressed(mc, mouseX, mouseY)) ? 243 : 230;
                int y = (!this.enabled || this.hovered) ? 43 : 30;
                this.drawTexturedModalRect(xPosition, yPosition, x, y, 1, buttonHeight);
                for (int i = 1; i < buttonWidth; i++)
                    this.drawTexturedModalRect(xPosition + i, yPosition, x + 1, y, 1, buttonHeight);
                this.drawTexturedModalRect(xPosition + buttonWidth, yPosition, x + 13, y, 1, buttonHeight);
                this.drawTexturedModalRect(xPosition + buttonWidth - 12, yPosition, (isAccept) ? 243 : 230, 56, 13, 13);
                this.mouseDragged(mc, mouseX, mouseY);
                int j = 14737632;

                if (packedFGColour != 0)
                {
                    j = packedFGColour;
                }
                else
                if (!this.enabled)
                {
                    j = 10526880;
                }
                else if (this.hovered)
                {
                    j = 16777120;
                }
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
            }
        }
    }
}
