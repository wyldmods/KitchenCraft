package org.wyldmods.kitchencraft.foods.common.config;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import net.minecraftforge.common.config.Configuration;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodTypeDropped;
import org.wyldmods.kitchencraft.foods.common.config.json.ShapedJsonRecipe;
import org.wyldmods.kitchencraft.foods.common.config.json.ShapelessJsonRecipe;
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
    static File configFile, foodJson, smeltingJson, shapelessRecipesJson, shapedRecipesJson;

    public static boolean doFoodTooltips = true;

    static final String foodJsonName = "foodAdditions.json";
    static final String smeltingJsonName = "smeltingAdditions.json";
    static final String shaplessRecipesJsonName = "shapelessRecipeAdditions.json";
    static final String shapedRecipesJsonName = "shapedRecipeAdditions.json";

    static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void preInit(File file)
    {
        parentDir = new File(file.getParent() + "/KitchenCraft");

        configFile = new File(parentDir.getAbsolutePath() + "/" + file.getName());
        foodJson = new File(parentDir.getAbsolutePath() + "/" + foodJsonName);
        smeltingJson = new File(parentDir.getAbsolutePath() + "/" + smeltingJsonName);
        shapelessRecipesJson = new File(parentDir.getAbsolutePath() + "/" + shaplessRecipesJsonName);
        shapedRecipesJson = new File(parentDir.getAbsolutePath() + "/" + shapedRecipesJsonName);

        Configuration config = new Configuration(configFile);

        try
        {
            loadStandardConfig(config);
            copyTextures();
            copyLang();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error loading KitchenCraft configs");
        }
    }

    public static void init()
    {
        try
        {
            loadFoodJson();
            loadSmeltingJson();
            loadShapelessRecipesJson();
            loadShapedRecipesJson();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error loading KitchenCraft smelting");
        }
    }

    private static void copyTextures() throws IOException
    {
        KitchenCraftFoods.logger.info("Attempting to load textures from config folder,");

        File iconDir = new File(parentDir.getAbsolutePath() + "/icons");

        if (!iconDir.exists())
        {
            iconDir.mkdir();
        }

        FileFilter pngFilter = FileFilterUtils.suffixFileFilter(".png");

        File[] icons = iconDir.listFiles(pngFilter);
        File itemIcons = getFileFromURL(KitchenCraftFoods.class.getResource("/assets/kitchencraft/textures/items"));
        File blockIcons = getFileFromURL(KitchenCraftFoods.class.getResource("/assets/kitchencraft/textures/blocks"));

        for (File icon : icons)
        {
            FileUtils.copyFile(icon, new File(itemIcons.getAbsolutePath() + "/" + icon.getName()));
            FileUtils.copyFile(icon, new File(blockIcons.getAbsolutePath() + "/" + icon.getName()));
        }
    }

    private static void copyLang() throws IOException
    {
        File jarLangDir = getFileFromURL(KitchenCraftFoods.class.getResource("/assets/kitchencraft/lang"));
        File cfgLangDir = new File(parentDir.getAbsolutePath() + "/lang");

        if (!cfgLangDir.exists())
        {
            cfgLangDir.mkdir();
        }

        System.out.println(jarLangDir.getAbsolutePath());

        FileFilter langFilter = FileFilterUtils.suffixFileFilter(".lang");

        File[] cfgLangs = cfgLangDir.listFiles(langFilter);

        System.out.println(cfgLangs);

//        URL u = KitchenCraftFoods.class.getResource("/assets/kitchencraft/lang/en_US.lang");
//        File inJar = getFileFromURL(u);
//        if (!fileExistsIn(inJar, cfgLangs))
//        {
//            FileUtils.copyFile(inJar, new File(cfgLangDir.getAbsolutePath() + "/" + inJar.getName()));
//        }

        for (File inCfg : cfgLangs)
        {
            FileUtils.copyFile(inCfg, new File(jarLangDir.getAbsolutePath() + "/" + inCfg.getName()));
        }
    }

    private static File getFileFromURL(URL url)
    {
        String urlPath = url.toString();

        urlPath = urlPath.replace("jar:", ""); // nope

        URL newUrl = null;
        try
        {
            newUrl = new URL(urlPath);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }

        return FileUtils.toFile(newUrl);
    }

    private static boolean fileExistsIn(File file, File[] toCheck)
    {
        for (File f : toCheck)
        {
            if (file.getName().equals(f.getName()))
            {
                return true;
            }
        }
        return false;
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
            ShapelessJsonRecipe.addShapelessRecipeFromJson(gson.fromJson(arr.get(i), ShapelessJsonRecipe.class));
        }
    }

    private static void loadShapedRecipesJson() throws IOException
    {
        JsonObject recipes = initializeJson(shapedRecipesJsonName, shapedRecipesJson);

        JsonArray arr = (JsonArray) recipes.get("recipes");
        for (int i = 0; i < arr.size(); i++)
        {
            ShapedJsonRecipe.addShapedRecipeFromJson(gson.fromJson(arr.get(i), ShapedJsonRecipe.class));
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
        KitchenCraftFoods.logger.info("Copying file " + filename + " from jar");
        URL url = KitchenCraftFoods.class.getResource("/assets/kitchencraft/misc/" + filename);
        FileUtils.copyURLToFile(url, to);
    }

    private static void loadStandardConfig(Configuration config)
    {
        config.load();

        doFoodTooltips = config.get(Configuration.CATEGORY_GENERAL, "doFoodTooltips", doFoodTooltips, "Show hunger and saturation tooltips on food.").getBoolean();

        config.save();
    }
}
