package org.wyldmods.kitchencraft.foods.common.config;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodTypeDropped;
import org.wyldmods.kitchencraft.foods.common.config.json.JsonRecipeUtils;
import org.wyldmods.kitchencraft.foods.common.config.json.ShapedJsonRecipe;
import org.wyldmods.kitchencraft.foods.common.config.json.ShapelessJsonRecipe;
import org.wyldmods.kitchencraft.foods.common.config.json.SmeltingRecipeJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import cpw.mods.fml.common.FMLCommonHandler;

public class ConfigurationHandler
{
    static File parentDir;
    static File configFile, foodJson, smeltingJson, shapelessRecipesJson, shapedRecipesJson, oreDictJson;

    public static boolean doFoodTooltips = true;

    static final String foodJsonName = "foodAdditions.json";
    static final String smeltingJsonName = "smeltingAdditions.json";
    static final String shaplessRecipesJsonName = "shapelessRecipeAdditions.json";
    static final String shapedRecipesJsonName = "shapedRecipeAdditions.json";
    static final String oreDictJsonName = "oreDictAdditions.json";

    static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void preInit(File file)
    {
        parentDir = new File(file.getParent() + "/KitchenCraft");

        configFile = new File(parentDir.getAbsolutePath() + "/" + file.getName());
        foodJson = new File(parentDir.getAbsolutePath() + "/" + foodJsonName);
        smeltingJson = new File(parentDir.getAbsolutePath() + "/" + smeltingJsonName);
        shapelessRecipesJson = new File(parentDir.getAbsolutePath() + "/" + shaplessRecipesJsonName);
        shapedRecipesJson = new File(parentDir.getAbsolutePath() + "/" + shapedRecipesJsonName);
        oreDictJson = new File(parentDir.getAbsoluteFile() + "/" + oreDictJsonName);

        Configuration config = new Configuration(configFile);

        try
        {
            loadStandardConfig(config);

            if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            {
                ResourcePackAssembler assembler = new ResourcePackAssembler(new File(parentDir.getAbsolutePath() + "/KC-Resource-Pack"));
                buildTextures(assembler);
                buildLang(assembler);
                assembler.assemble();
                assembler.inject(new File(parentDir.getParentFile().getParentFile().getAbsolutePath() + "/resourcepacks"));
            }
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
            iconDir.mkdir();
        }

        FileFilter pngFilter = FileFilterUtils.suffixFileFilter(".png");

        File[] icons = iconDir.listFiles(pngFilter);

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
            cfgLangDir.mkdir();
        }

        FileFilter langFilter = FileFilterUtils.suffixFileFilter(".lang");

        File[] cfgLangs = cfgLangDir.listFiles(langFilter);

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
            OreDictionary.registerOre(entry.name, (ItemStack) JsonRecipeUtils.parseStringIntoRecipeItem(entry.item, true));
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

    private static JsonObject initializeJson(String filename, File f) throws IOException
    {
        if (!f.exists())
            copyFromJar(filename, f);

        Reader in = new FileReader(f);

        return (JsonObject) new JsonParser().parse(in);
    }

    static void copyFromJar(String filename, File to) throws IOException
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
    
    public static class OreDictEntry
    {
        public String item;
        public String name;
    }
}
