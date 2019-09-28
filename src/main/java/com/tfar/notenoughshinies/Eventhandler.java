package com.tfar.notenoughshinies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Eventhandler {

    @SubscribeEvent
    public static void handleEntityItemPickup(EntityItemPickupEvent event) {
      EntityPlayer player = event.getEntityPlayer();
      if (player.openContainer instanceof WalletContainer) {
        return;
      }
      InventoryPlayer inventory = player.inventory;
      for (int i = 0; i < inventory.getSizeInventory(); i++) {
        ItemStack stack = inventory.getStackInSlot(i);
        if (stack.getItem() instanceof WalletItem && WalletItem.onItemPickup(event, stack)) {
          event.setCanceled(true);
          return;
        }
      }
    }
  }