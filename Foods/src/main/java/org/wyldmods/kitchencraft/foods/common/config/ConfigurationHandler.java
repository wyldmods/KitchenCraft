package org.wyldmods.kitchencraft.foods.common.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.io.FileUtils;
import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodModification;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodTypeDropped;
import org.wyldmods.kitchencraft.foods.common.config.json.ShapedJsonRecipe;
import org.wyldmods.kitchencraft.foods.common.config.json.ShapelessJsonRecipe;
import org.wyldmods.kitchencraft.foods.common.config.json.SmeltingRecipeJson;

import tterrag.core.common.json.JsonUtils;
import tterrag.core.common.util.IOUtils;
import tterrag.core.common.util.ResourcePackAssembler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigurationHandler
{
    static File parentDir;
    static File configFile, foodJson, smeltingJson, shapelessRecipesJson, shapedRecipesJson, oreDictJson, foodModsJson;

    public static boolean doFoodTooltips = true;

    static final String foodJsonName            = "foodAdditions.json";
    static final String smeltingJsonName        = "smeltingAdditions.json";
    static final String shaplessRecipesJsonName = "shapelessRecipeAdditions.json";
    static final String shapedRecipesJsonName   = "shapedRecipeAdditions.json";
    static final String oreDictJsonName         = "oreDictAdditions.json";
    static final String foodModsJsonName        = "foodModifications.json";

    static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void preInit(File file)
    {
        parentDir = new File(file.getParent() + "/KitchenCraft");

        configFile = new File(parentDir.getAbsolutePath() + "/" + file.getName());
        foodJson = new File(parentDir.getAbsolutePath() + "/" + foodJsonName);
        smeltingJson = new File(parentDir.getAbsolutePath() + "/" + smeltingJsonName);
        shapelessRecipesJson = new File(parentDir.getAbsolutePath() + "/" + shaplessRecipesJsonName);
        shapedRecipesJson = new File(parentDir.getAbsolutePath() + "/" + shapedRecipesJsonName);
        oreDictJson = new File(parentDir.getAbsolutePath() + "/" + oreDictJsonName);
        foodModsJson = new File(parentDir.getAbsolutePath() + "/" + foodModsJsonName);

        Configuration config = new Configuration(configFile);

        try
        {
            loadStandardConfig(config);
            initializeDefaults();
            
            ResourcePackAssembler assembler = new ResourcePackAssembler(new File(parentDir.getAbsolutePath() + "/KC-Resource-Pack"), "KitchenCraft-Foods Resource Pack",
                    Reference.MOD_TEXTUREPATH).setHasPackPng(KitchenCraftFoods.class);
            buildTextures(assembler);
            buildLang(assembler);
            assembler.assemble().inject();
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
            loadOreDictJson();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void postInit()
    {
        try
        {
            loadSmeltingJson();
            loadShapelessRecipesJson();
            loadShapedRecipesJson();
            loadFoodMods();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void reload()
    {
        FoodType.validAnimals.clear();
        FoodType.validBlocks.clear();

        FoodType.veggies.clear();
        FoodType.meats.clear();

        try
        {
            loadFoodJson();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void buildTextures(ResourcePackAssembler assembler) throws IOException
    {
        File iconDir = new File(parentDir.getAbsolutePath() + "/icons");

        if (!iconDir.exists())
        {
            iconDir.mkdirs();
        }

        File[] icons = iconDir.listFiles(IOUtils.pngFilter);

        for (File icon : icons)
        {
            assembler.addIcon(icon);
        }
    }

    private static void buildLang(ResourcePackAssembler assembler) throws IOException
    {
        File cfgLangDir = new File(parentDir.getAbsolutePath() + "/lang");

        if (!cfgLangDir.exists())
        {
            cfgLangDir.mkdirs();
        }

        File[] cfgLangs = cfgLangDir.listFiles(IOUtils.langFilter);

        for (File inCfg : cfgLangs)
        {
            assembler.addLang(inCfg);
        }
    }

    private static void loadFoodJson() throws IOException
    {
        JsonObject foods = initializeJson(foodJsonName, foodJson);

        JsonArray arr = (JsonArray) foods.get("foods");
        for (int i = 0; i < arr.size(); i++)
        {
            FoodType.registerFoodType(gson.fromJson(arr.get(i), isDroppedFoodType(arr.get(i).getAsJsonObject()) ? FoodTypeDropped.class : FoodType.class));
        }
    }

    private static boolean isDroppedFoodType(JsonObject obj)
    {
        return obj.has("animals") || obj.has("blocks");
    }

    private static void loadOreDictJson() throws IOException
    {
        JsonObject entries = initializeJson(oreDictJsonName, oreDictJson);

        JsonArray arr = (JsonArray) entries.get("entries");
        for (int i = 0; i < arr.size(); i++)
        {
            OreDictEntry entry = gson.fromJson(arr.get(i), OreDictEntry.class);
            OreDictionary.registerOre(entry.name, (ItemStack) ConfigurationHandler.parseInputString(entry.item, true));
        }
    }

    private static void loadSmeltingJson() throws IOException
    {
        JsonObject recipes = initializeJson(smeltingJsonName, smeltingJson);

        JsonArray arr = (JsonArray) recipes.get("smelting");
        for (int i = 0; i < arr.size(); i++)
        {
            SmeltingRecipeJson.registerSmeltingRecipe(gson.fromJson(arr.get(i), SmeltingRecipeJson.class));
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
    
    private static void loadFoodMods() throws IOException
    {
        JsonObject mods = initializeJson(foodModsJsonName, foodModsJson);
        
        JsonArray arr = (JsonArray) mods.get("mods");
        for (int i = 0; i < arr.size(); i++)
        {
            FoodModification.registerModification(gson.fromJson(arr.get(i), FoodModification.class));
        }
    }

    private static JsonObject initializeJson(String filename, File f) throws IOException
    {
        if (!f.exists())
            copyFromJar(filename, f);

        Reader in = new FileReader(f);

        return (JsonObject) new JsonParser().parse(in);
    }

    static void copyFromJar(String filename, File to) throws IOException
    {
        IOUtils.copyFromJar(KitchenCraftFoods.class, "kitchencraft/misc/" + filename, to);
    }

    private static void loadStandardConfig(Configuration config)
    {
        config.load();

        doFoodTooltips = config.get(Configuration.CATEGORY_GENERAL, "doFoodTooltips", doFoodTooltips, "Show hunger and saturation tooltips on food.").getBoolean();

        config.save();
    }

    private static void initializeDefaults() throws IOException
    {
        File iconsDir = new File(parentDir.getAbsolutePath() + "/icons");
        File langDir = new File(parentDir.getAbsolutePath() + "/lang");
        File defaultZip = new File(parentDir.getAbsolutePath() + "/defaultPack.zip");
        
        boolean iconsExist = iconsDir.exists();
        boolean langsExist = langDir.exists();
        
        if (!iconsExist || !langsExist)
        {
            copyFromJar("defaultPack.zip", defaultZip);
            File output = IOUtils.extractZip(defaultZip);
            FileUtils.copyDirectory(new File(output.getAbsolutePath() + "/icons"), iconsDir);
            FileUtils.copyDirectory(new File(output.getAbsolutePath() + "/lang"), langDir);
            IOUtils.safeDeleteDirectory(output);
            IOUtils.safeDelete(defaultZip);
        }
    }

    public static Object parseInputString(String string)
    {
        return parseInputString(string, false);
    }

    public static Object parseInputString(String string, boolean forceItemStack)
    {
        ItemStack food = FoodType.getFood(string);
        return food == null ? JsonUtils.parseStringIntoRecipeItem(string, forceItemStack) : food;
    }

    public static class OreDictEntry
    {
        public String item;
        public String name;
    }
}
