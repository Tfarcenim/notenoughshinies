package com.tfar.notenoughshinies;

import com.tfar.notenoughshinies.network.CPacketWithdraw;
import com.tfar.notenoughshinies.network.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.io.IOException;

import static com.tfar.notenoughshinies.NotEnoughShinies.Objects.*;

public class WalletScreen extends GuiContainer {

  private static final ResourceLocation background = new ResourceLocation(NotEnoughShinies.MODID,"textures/gui/wallet.png");

  public int copper,silver,gold = 0;

  public WalletScreen(Container inventorySlotsIn) {
    super(inventorySlotsIn);
    this.ySize += 6;
  }

  @Override
  public void initGui() {
    super.initGui();
    addButton(new GuiButton(1, guiLeft + 110, guiTop + 14, 50,20, "Withdraw"));

  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    int offset1 = 50;
    int y = -9;
    WalletContainer walletContainer = (WalletContainer)inventorySlots;

    drawString("gold:",21,y += 13,0x404040);
    drawString(String.valueOf(Util.getTagSafely(walletContainer.wallet).getInteger("gold")),offset1,y,0x404040);

    drawString(String.valueOf(gold),offset1 + 30,y,0x404040);

    drawString("silver:",13,y += 13,0x404040);
    drawString(String.valueOf(Util.getTagSafely(walletContainer.wallet).getInteger("silver")),offset1,y,0x404040);

    drawString(String.valueOf(silver),offset1 + 30,y,0x404040);


    drawString("copper:",6,y += 13,0x404040);
    drawString(String.valueOf(Util.getTagSafely(walletContainer.wallet).getInteger("copper")),offset1,y,0x404040);

    drawString(String.valueOf(copper),offset1 + 30,y,0x404040);


  }

  private int drawString(String text, int x, int y, int color){
    return mc.fontRenderer.drawString(text,x,y,color);
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    int i = Mouse.getEventDWheel();
    if (i == 0)return;
    i/=120;

    final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
    int i1 = scaledresolution.getScaledWidth();
    int j1 = scaledresolution.getScaledHeight();
    final int x = Mouse.getX() * i1 / this.mc.displayWidth;
    final int y = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;

    int offsetx = 80;

    int maxGold = Util.getTagSafely(((WalletContainer) inventorySlots).wallet).getInteger("gold");

    boolean isScrollingGold = x > guiLeft + offsetx && x < guiLeft + offsetx + 20 && y > guiTop && y < guiTop + 13;

    if (isScrollingGold) {
      gold += i;
      gold = MathHelper.clamp(gold, 0, Util.getTagSafely(((WalletContainer) inventorySlots).wallet).getInteger("gold"));
    }

    boolean isMaxGold = maxGold == gold;

    boolean isScrollingSilver = x > guiLeft + offsetx && x < guiLeft + offsetx + 20 && y > guiTop + 13 && y < guiTop + 26;

    if (isScrollingSilver) {
      silver += i;
      silver = MathHelper.clamp(silver, 0, ModConfig.silver_gold);
    }

    boolean isScrollingCopper = x > guiLeft + offsetx && x < guiLeft + offsetx + 20 && y > guiTop + 26 && y < guiTop + 39;

    if (isScrollingCopper) {
      copper += i;
      copper = MathHelper.clamp(copper, 0, ModConfig.silver_gold);
    }

    if (isMaxGold){
      copper = Math.min(copper,Util.getTagSafely(((WalletContainer) inventorySlots).wallet).getInteger("copper"));
      silver = Math.min(silver,Util.getTagSafely(((WalletContainer) inventorySlots).wallet).getInteger("silver"));
    }

  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 1) {

      ItemStack goldcoins = new ItemStack(gold_coin,gold);
      Util.removeCoins(goldcoins,((WalletContainer)inventorySlots).wallet);

      ItemStack silvercoins = new ItemStack(silver_coin,silver);
      Util.removeCoins(silvercoins,((WalletContainer)inventorySlots).wallet);

      ItemStack coppercoins = new ItemStack(copper_coin,copper);
      Util.removeCoins(coppercoins,((WalletContainer)inventorySlots).wallet);

      PacketHandler.INSTANCE.sendToServer(new CPacketWithdraw(copper,silver,gold));
      copper = silver = gold = 0;
    }
  }

  /**
   * Draws the background layer of this container (behind the items).
   *
   * @param partialTicks
   * @param mouseX
   * @param mouseY
   */
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    mc.getTextureManager().bindTexture(background);
    int i = this.guiLeft;
    int j = (this.height - this.ySize) / 2 - 7;
    this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
  }

}
