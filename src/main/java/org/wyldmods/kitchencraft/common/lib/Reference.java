package org.wyldmods.kitchencraft.common.lib;

public class Reference
{
    public static final String MOD_ID_MACHINES = "kitchenCraftMachines";
    public static final String MOD_NAME_MACHINES = "KitchenCraft - Machines";
    public static final String CLIENT_PROXY_CLASS_MACHINES = "org.wyldmods.kitchencraft.machines.client.ClientProxy";
    public static final String SERVER_PROXY_CLASS_MACHINES = "org.wyldmods.kitchencraft.machines.common.CommonProxy";

    public static final String MOD_ID_FOODS = "kitchenCraftFoods";
    public static final String MOD_NAME_FOODS = "KitchenCraft - Foods";
    public static final String CLIENT_PROXY_CLASS_FOODS = "org.wyldmods.kitchencraft.foods.client.ClientProxy";
    public static final String SERVER_PROXY_CLASS_FOODS = "org.wyldmods.kitchencraft.foods.common.CommonProxy";
    
    public static final String MOD_TEXTUREPATH = "kitchencraft";
    public static final String VERSION = "0.0.1";
    
    public static final String DEPENDENCIES = "required-after:ttCore@1.7.10-0.0.2-5,);"
                                            + "after:Waila;"
                                            + "after:EnderIO";
}
