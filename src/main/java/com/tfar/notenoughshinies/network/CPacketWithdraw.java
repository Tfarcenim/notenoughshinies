package com.tfar.notenoughshinies.network;

import com.tfar.notenoughshinies.Util;
import com.tfar.notenoughshinies.WalletContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static com.tfar.notenoughshinies.NotEnoughShinies.Objects.*;

public class CPacketWithdraw  implements IMessage {

  int copper;
  int silver;
  int gold;

  public CPacketWithdraw() {}

    public CPacketWithdraw(int copper,int silver, int gold) {
      this.copper = copper;
      this.silver = silver;
      this.gold = gold;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      copper = buf.readInt();
      silver = buf.readInt();
      gold = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
      buf.writeInt(copper);
      buf.writeInt(silver);
      buf.writeInt(gold);
    }

    public static class Handler implements IMessageHandler<CPacketWithdraw, IMessage> {
      @Override
      public IMessage onMessage(CPacketWithdraw message, MessageContext ctx) {
        // Always use a construct like this to actually handle your message. This ensures that
        // youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
        // is called on the networking thread so it is not safe to do a lot of things
        // here.
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
        return null;
      }

      private void handle(CPacketWithdraw message, MessageContext ctx) {
        // This code is run on the server side. So you can do server-side calculations here
        EntityPlayerMP playerMP = ctx.getServerHandler().player;
        Container container = playerMP.openContainer;
        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
        mainThread.addScheduledTask(() -> {
          if (container instanceof WalletContainer) {
            ItemStack wallet = playerMP.getHeldItemMainhand();
            ItemStack goldcoins = new ItemStack(gold_coin,message.gold);
            Util.removeCoins(goldcoins,wallet);
            if (!playerMP.addItemStackToInventory(goldcoins)) {
              EntityItem entityItem = new EntityItem(playerMP.world, playerMP.posX, playerMP.posY, playerMP.posZ, goldcoins);
              playerMP.world.spawnEntity(entityItem);
            }

            ItemStack silvercoins = new ItemStack(silver_coin,message.silver);
            Util.removeCoins(silvercoins,wallet);
            if (!playerMP.addItemStackToInventory(silvercoins)) {
              EntityItem entityItem = new EntityItem(playerMP.world, playerMP.posX, playerMP.posY, playerMP.posZ, silvercoins);
              playerMP.world.spawnEntity(entityItem);
            }

            ItemStack coppercoins = new ItemStack(copper_coin,message.copper);
            Util.removeCoins(coppercoins,wallet);
            if (!playerMP.addItemStackToInventory(coppercoins)) {
              EntityItem entityItem = new EntityItem(playerMP.world, playerMP.posX, playerMP.posY, playerMP.posZ, coppercoins);
              playerMP.world.spawnEntity(entityItem);
            }
          }
        });
      }
    }
  }
