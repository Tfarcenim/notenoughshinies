package com.tfar.notenoughshinies;

import net.minecraftforge.common.config.Config;

@Config(modid = NotEnoughShinies.MODID)
public class ModConfig {

  @Config.Name("copper:silver conversion rate")
  public static int copper_silver = 64;

  @Config.Name("silver:gold conversion rate")
  public static int silver_gold = 64;


}
