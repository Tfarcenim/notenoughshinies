package com.tfar.notenoughshinies;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static com.tfar.notenoughshinies.NotEnoughShinies.Objects.*;

public class Util {
  public static NBTTagCompound getTagSafely(ItemStack stack) {
    return stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
  }

  public static void addCoins(ItemStack coins, ItemStack wallet) {
    final Item coin = coins.getItem();
    NBTTagCompound nbt = getTagSafely(wallet);
    if (coin == copper_coin) {
      int current = nbt.getInteger("copper");
      nbt.setInteger("copper", current + coins.getCount());
    } else if (coin == silver_coin) {
      int current = nbt.getInteger("silver");
      nbt.setInteger("silver", current + coins.getCount());
    } else if (coin == gold_coin) {
      int current = nbt.getInteger("gold");
      nbt.setInteger("gold", current + coins.getCount());
    }
    //wallet.setTagCompound(nbt);
    merge(wallet);
  }

  public static void removeCoins(ItemStack coins, ItemStack wallet) {
    final Item coin = coins.getItem();
    NBTTagCompound nbt = getTagSafely(wallet);
    int subtract = coins.getCount();
    if (coin == copper_coin) {
      int currentcoppercoins = nbt.getInteger("copper");
      if (currentcoppercoins >= subtract)
      nbt.setInteger("copper", currentcoppercoins - subtract);
      else {
        currentcoppercoins += ModConfig.copper_silver;
        nbt.setInteger("copper", currentcoppercoins - subtract);

        int currentsilvercoins = nbt.getInteger("silver");
        if (currentsilvercoins > 0)
        nbt.setInteger("silver", currentsilvercoins - 1);
        else {
          currentsilvercoins += ModConfig.silver_gold;
          nbt.setInteger("silver", currentsilvercoins - 1);
          nbt.setInteger("gold", nbt.getInteger("gold") - 1);
        }
      }

    } else if (coin == silver_coin) {
      int currentsilvercoins = nbt.getInteger("silver");
      if (currentsilvercoins >= subtract)
        nbt.setInteger("silver", currentsilvercoins - subtract);
      else {
        currentsilvercoins += ModConfig.silver_gold;
        nbt.setInteger("silver", currentsilvercoins - subtract);

        int currentgoldcoins = nbt.getInteger("gold");
        nbt.setInteger("gold", currentgoldcoins - 1);
      }
    } else if (coin == gold_coin) {
      int current = nbt.getInteger("gold");
      nbt.setInteger("gold", current - subtract);
    }
    wallet.setTagCompound(nbt);
  }

  public static void merge(ItemStack wallet) {
    NBTTagCompound nbt = getTagSafely(wallet);
    int coppercount = nbt.getInteger("copper");
    int silvercount = nbt.getInteger("silver");
    int goldcount = nbt.getInteger("gold");

    int coppertosilver = coppercount / ModConfig.copper_silver;
    int removecopper = coppertosilver * ModConfig.copper_silver;

    nbt.setInteger("copper", coppercount - removecopper);
    nbt.setInteger("silver", silvercount += coppertosilver);

    int silvertogold = silvercount / ModConfig.silver_gold;
    int removesilver = silvertogold * ModConfig.silver_gold;

    nbt.setInteger("silver", silvercount - removesilver);
    nbt.setInteger("gold", silvertogold + goldcount);
  }
}
