package org.wyldmods.kitchencraft.foods.common.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import net.minecraftforge.common.config.Configuration;

import org.apache.commons.io.FileUtils;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodTypeDropped;
import org.wyldmods.kitchencraft.foods.common.config.json.ShapelessFoodRecipeJson;
import org.wyldmods.kitchencraft.foods.common.config.json.SmeltingRecipeJson;
import org.wyldmods.kitchencraft.foods.common.item.KCItems;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigurationHandler
{
    static File parentDir;
    static File configFile, foodJson, smeltingJson, shapelessRecipesJson;
    static final String foodJsonName = "foodAdditions.json";
    static final String smeltingJsonName = "smeltingAdditions.json";
    static final String shaplessRecipesJsonName = "shapelessFoodRecipes.json";
    static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void init(File file)
    {
        parentDir = new File(file.getParent() + "/KitchenCraft");

        configFile = new File(parentDir.getAbsolutePath() + "/" + file.getName());
        foodJson = new File(parentDir.getAbsoluteFile() + "/" + foodJsonName);
        smeltingJson = new File(parentDir.getAbsoluteFile() + "/" + smeltingJsonName);
        shapelessRecipesJson = new File(parentDir.getAbsoluteFile() + "/" + shaplessRecipesJsonName);

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

    public static void postInit()
    {
        try
        {
            loadSmeltingJson();
            loadShapelessRecipesJson();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error loading KitchenCraft smelting");
        }
    }

    private static void loadFoodJson() throws IOException
    {
        JsonObject foods = initializeJson(foodJsonName, foodJson);

        JsonArray arr = (JsonArray) foods.get("foods");
        for (int i = 0; i < arr.size(); i++)
        {
            FoodType.registerFoodType(gson.fromJson(arr.get(i), arr.get(i).getAsJsonObject().get("animals") != null ? FoodTypeDropped.class : FoodType.class));
        }
    }

    private static void loadSmeltingJson() throws IOException
    {
        JsonObject recipes = initializeJson(smeltingJsonName, smeltingJson);

        JsonArray arr = (JsonArray) recipes.get("smelting");
        for (int i = 0; i < arr.size(); i++)
        {
            KCItems.registerSmeltingRecipe(gson.fromJson(arr.get(i), SmeltingRecipeJson.class));
        }
    }

    private static void loadShapelessRecipesJson() throws IOException
    {
        JsonObject recipes = initializeJson(shaplessRecipesJsonName, shapelessRecipesJson);

        JsonArray arr = (JsonArray) recipes.get("recipes");
        for (int i = 0; i < arr.size(); i++)
        {
            ShapelessFoodRecipeJson.addShapelessRecipeFromJson(gson.fromJson(arr.get(i), ShapelessFoodRecipeJson.class));
        }
    }

    private static JsonObject initializeJson(String filename, File f) throws IOException
    {
        if (!f.exists())
            copyJsonFromJar(filename, f);

        String json = "";

        Scanner scan = new Scanner(f);
        while (scan.hasNextLine())
        {
            json += scan.nextLine() + "\n";
        }
        scan.close();

        return (JsonObject) new JsonParser().parse(json);
    }

    private static void copyJsonFromJar(String filename, File to) throws IOException
    {
        System.out.println("Copying file " + filename + " from jar");
        URL url = KitchenCraftFoods.class.getResource("/assets/kitchencraft/misc/" + filename);
        FileUtils.copyURLToFile(url, to);
    }

    private static void loadStandardConfig(Configuration config)
    {
        config.load();

        // TODO config options

        config.save();
    }
}
