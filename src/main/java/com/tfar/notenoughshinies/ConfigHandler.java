package com.tfar.notenoughshinies;

import com.google.gson.*;

import java.io.*;

public class ConfigHandler {

  public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  public static final String location = "config/" + NotEnoughShinies.MODID + ".json";

  public static void handleConfig(){
    File file = new File(location);
    if (!file.exists())writeConfig();
    NotEnoughShinies.coinEntries = readConfig();
  }

  public static void writeConfig() {
    try {
      FileWriter writer = new FileWriter(location);
      writer.write(ExampleConfig.s);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static CoinEntry[] readConfig() {
    try {
      Reader reader = new FileReader(location);
      return g.fromJson(reader, CoinEntry[].class);
    } catch (IOException ofcourse) {
      throw new RuntimeException(ofcourse);
    }
  }
}

