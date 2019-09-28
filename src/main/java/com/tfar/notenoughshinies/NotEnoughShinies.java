package com.tfar.notenoughshinies;

import com.tfar.notenoughshinies.network.PacketHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static com.tfar.notenoughshinies.NotEnoughShinies.Objects.*;

@Mod.EventBusSubscriber(modid = NotEnoughShinies.MODID)
@Mod(modid = NotEnoughShinies.MODID, name = NotEnoughShinies.NAME, version = NotEnoughShinies.VERSION)
public class NotEnoughShinies {
  public static final String MODID = "notenoughshinies";
  public static final String NAME = "Example Mod";
  public static final String VERSION = "1.0";
  private static final Set<Item> MOD_ITEMS = new HashSet<>();
  public static CoinEntry[] coinEntries;

  private static Logger logger;

  @Mod.Instance
  public static NotEnoughShinies instance;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    PacketHandler.registerMessages(MODID);
    NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
  }

  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

    register(new Item(), "copper_coin", registry);
    register(new Item(), "silver_coin", registry);
    register(new Item(), "gold_coin", registry);
    register(new WalletItem(), "wallet", registry);

  }

  @GameRegistry.ObjectHolder(MODID)
  public static class Objects {
    public static final Item copper_coin = null;
    public static final Item silver_coin = null;
    public static final Item gold_coin = null;
    public static final Item wallet = null;
  }

  private static <T extends IForgeRegistryEntry<T>> void register(T entry, String name, IForgeRegistry<T> registry) {
    entry.setRegistryName(new ResourceLocation(MODID, name));
    if (entry instanceof Item) {
      ((Item) entry).setTranslationKey(entry.getRegistryName().toString());
      ((Item) entry).setCreativeTab(CreativeTabs.TOOLS);
      MOD_ITEMS.add((Item) entry);
    }
    registry.register(entry);
  }

  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    for (Item item : MOD_ITEMS)
      registerModelLocation(item);
  }

  private static void registerModelLocation(Item item) {
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
  }

  @SubscribeEvent
  public static void onEvent(LootTableLoadEvent event) {
    ResourceLocation name = event.getName();
    if (!name.getPath().startsWith("entities")) return;
    ConfigHandler.handleConfig();
    for (CoinEntry entry : coinEntries) {
      String s1 = name.getPath().split("/")[1];
      ResourceLocation rl = new ResourceLocation(name.getNamespace(), s1);
      if (rl.equals(new ResourceLocation(entry.mob))) {

        LootFunction lootFunctionCopper = new SetCount(new LootCondition[0], new RandomValueRange(entry.copper));
        LootFunction[] lootFunctionsCopper = new LootFunction[]{lootFunctionCopper};
        LootEntry lootEntryCopper = new LootEntryItem(copper_coin, 1, 1, lootFunctionsCopper, new LootCondition[0], "copper");

        LootFunction lootFunctionSilver = new SetCount(new LootCondition[0], new RandomValueRange(entry.silver));
        LootFunction[] lootFunctionsSilver = new LootFunction[]{lootFunctionSilver};
        LootEntry lootEntrySilver = new LootEntryItem(silver_coin, 1, 1, lootFunctionsSilver, new LootCondition[0], "silver");

        LootFunction lootFunctionGold = new SetCount(new LootCondition[0], new RandomValueRange(entry.gold));
        LootFunction[] lootFunctionsGold = new LootFunction[]{lootFunctionGold};
        LootEntry lootEntryGold = new LootEntryItem(gold_coin, 1, 1, lootFunctionsGold, new LootCondition[0], "gold");


        LootEntry[] lootEntries = new LootEntry[]{lootEntryCopper, lootEntrySilver, lootEntryGold};
        LootPool pool = new LootPool(lootEntries, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), "coin_pool");
        LootTable table = event.getTable();
        table.addPool(pool);
      }
    }
  }
}
