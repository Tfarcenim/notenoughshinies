package com.tfar.notenoughshinies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class WalletItem extends Item {

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    if (!worldIn.isRemote)
    playerIn.openGui(NotEnoughShinies.instance, 0, worldIn, 0, 0, 0);
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }

  public static boolean onItemPickup(EntityItemPickupEvent event, ItemStack bag) {

    ItemStack toPickup = event.getItem().getItem();

    if (!WalletContainer.isCoin(toPickup)) {
      return false;
    }
    ItemStack rem = toPickup.copy();
    Util.addCoins(rem,bag,false);

    //leftovers
   // toPickup.setCount(rem.getCount());
    if (/*rem.getCount() != count*/true) {
      bag.setAnimationsToGo(5);
      EntityPlayer player = event.getEntityPlayer();
      player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
    }
    toPickup.setCount(0);
    return true;
  }
}
