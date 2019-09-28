package com.tfar.notenoughshinies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static com.tfar.notenoughshinies.NotEnoughShinies.Objects.*;

public class WalletContainer extends Container {

  public final World world;
  public final EntityPlayer player;

  public final ItemStack wallet;


  public WalletContainer(InventoryPlayer inventory, World world, ItemStack wallet) {
    this.world = world;
    this.player = inventory.player;
    this.wallet = wallet;

    for (int k = 0; k < 3; ++k) {
      for (int i1 = 0; i1 < 9; ++i1) {
        this.addSlotToContainer(new Slot(inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
      }
    }

    for (int l = 0; l < 9; ++l) {
      this.addSlotToContainer(new Slot(inventory, l, 8 + l * 18, 142));
    }
  }

  /**
   * Determines whether supplied player can use this container
   *
   * @param playerIn
   */
  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;
  }

  /**
   * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
   * inventory and the other inventory(s).
   */
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);

    if (slot != null) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      }

      else if (isCoin(itemstack)){
        Util.addCoins(itemstack,wallet);
        slot.putStack(ItemStack.EMPTY);
      }
      else /*if (!this.mergeItemStack(itemstack1, 0, 36, false))*/
      {
        slot.onSlotChanged();
        return ItemStack.EMPTY;
      }

      //else {
      //}
    }

    return itemstack;
  }

  @SuppressWarnings("ConstantConditions")
  public static boolean isCoin(ItemStack stack){
    return stack.getItem() == copper_coin || stack.getItem() == silver_coin || stack.getItem() == gold_coin;
  }
}
