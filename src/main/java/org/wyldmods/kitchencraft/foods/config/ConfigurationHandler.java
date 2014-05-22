package org.wyldmods.kitchencraft.foods.config;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import net.minecraftforge.common.config.Configuration;

import org.apache.commons.io.FileUtils;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.item.FoodType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigurationHandler
{
    static File configFile, foodJson;

    public static void init(File file)
    {
        File parentDir = new File(file.getParent() + "/KitchenCraft");

        configFile = new File(parentDir.getAbsolutePath() + "/" + file.getName());
        foodJson = new File(parentDir.getAbsoluteFile() + "/foodAdditions.json");

        Configuration config = new Configuration(configFile);

        try
        {
            loadStandardConfig(config);
            loadFoodJson();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error loading KitchenCraft configs");
        }
    }

    private static void loadFoodJson() throws IOException
    {
        if (!foodJson.exists())
            copyJsonFromJar();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = "";
        
        Scanner scan = new Scanner(foodJson);
        while (scan.hasNextLine())
        {
            json += scan.nextLine() + "\n";
        }
        scan.close();
        
        JsonObject obj = (JsonObject) new JsonParser().parse(json);
        JsonArray arr = (JsonArray) obj.get("foods");

        for (int i = 0; i < arr.size(); i++)
        {
            FoodType.registerFoodType(gson.fromJson(arr.get(i), FoodType.class));
        }
    }

    private static void copyJsonFromJar() throws IOException
    {
        File jsonFile = new File(KitchenCraftFoods.class.getResource("/assets/kitchencraft/misc/foodAdditions.json").getFile());
        FileUtils.copyFile(jsonFile, foodJson);
    }

    private static void loadStandardConfig(Configuration config)
    {
        config.load();

        // TODO config options

        config.save();
    }
}
