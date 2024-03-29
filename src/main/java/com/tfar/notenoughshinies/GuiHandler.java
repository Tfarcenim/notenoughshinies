package com.tfar.notenoughshinies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      if (ID == 0) {
        if (player.getHeldItemMainhand().getItem() instanceof WalletItem)
          return new WalletContainer(player.inventory, world, player.getHeldItemMainhand());
      }
      return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      if (ID == 0) {
        if (player.getHeldItemMainhand().getItem() instanceof WalletItem)
          return new WalletScreen(new WalletContainer(player.inventory, world, player.getHeldItemMainhand()));
      }
      return null;
    }
  }


