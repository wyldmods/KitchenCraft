package org.wyldmods.kitchencraft.foods.common.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.wyldmods.kitchencraft.common.lib.Reference;

public class ResourcePackAssembler
{
    private List<File> icons = new ArrayList<File>();
    private List<File> langs = new ArrayList<File>();

    private static final String NAME = "KitchenCraft-Foods Resource Pack";
    private static final String MC_META = "{\"pack\":{\"pack_format\":1,\"description\":\"" + NAME + "\"}}";

    private File dir;

    public ResourcePackAssembler(File directory)
    {
        dir = directory;
    }

    public void addIcon(File icon)
    {
        icons.add(icon);
    }

    public void addLang(File lang)
    {
        langs.add(lang);
    }

    public ResourcePackAssembler assemble() throws IOException
    {
        String pathToDir = dir.getAbsolutePath();
        File mcmeta = new File(pathToDir + "/pack.mcmeta");
        writeDefaultMcmeta(mcmeta);

        String itemsDir = pathToDir + "/assets/" + Reference.MOD_TEXTUREPATH + "/textures/items";
        String blocksDir = pathToDir + "/assets/" + Reference.MOD_TEXTUREPATH + "/textures/blocks";
        String langDir = pathToDir + "/assets/" + Reference.MOD_TEXTUREPATH + "/lang";

        for (File icon : icons)
        {
            FileUtils.copyFile(icon, new File(itemsDir + "/" + icon.getName()));
            FileUtils.copyFile(icon, new File(blocksDir + "/" + icon.getName()));
        }

        for (File lang : langs)
        {
            FileUtils.copyFile(lang, new File(langDir + "/" + lang.getName()));
        }

        return this;
    }

    public void inject(File resourcePacksDir) throws IOException
    {
        FileUtils.copyDirectory(dir, new File(resourcePacksDir.getAbsolutePath() + "/" + dir.getName()));
    }

//    private void addInResourcePack(File file) throws IOException
//    {
//        // hacky mess, thanks FML
//        String s = "";
//        Scanner scan = new Scanner(file);
//        while (scan.hasNext())
//        {
//            String line = scan.nextLine();
//            if (line.startsWith("resourcePacks:"))
//            {
//                line = line.replace("[", "[\"" + dir.getName() + "\",");
//                if (line.charAt(line.length() - 2) == ',')
//                {
//                    line = line.replace(dir.getName() + "\",", dir.getName() + "\"");
//                }
//            }
//            s += line + "\n";
//        }
//        scan.close();
//
//        FileWriter fw = new FileWriter(file);
//        fw.write(s);
//        fw.flush();
//        fw.close();
//    }

    private void writeDefaultMcmeta(File file) throws IOException
    {
        file.delete();
        file.getParentFile().mkdirs();
        file.createNewFile();

        FileWriter fw = new FileWriter(file);
        fw.write(MC_META);
        fw.flush();
        fw.close();
    }
}
