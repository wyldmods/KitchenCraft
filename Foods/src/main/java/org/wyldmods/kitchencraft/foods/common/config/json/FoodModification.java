package org.wyldmods.kitchencraft.foods.common.config.json;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;
import net.minecraft.item.ItemStack;

import org.wyldmods.kitchencraft.foods.common.config.json.FoodType.PotionEntry;

import com.enderio.core.common.util.ItemUtil;

@ToString
public class FoodModification
{
    /* JSON Types @formatter:off */
    public String        item        = null;
    public PotionEntry[] effects     = {};
    public int           food        = -1;
    public float         saturation  = -1f;
    /* End JSON Types @formatter:on */
    
    private transient ItemStack toEffect;
    
    public static final List<FoodModification> types = new ArrayList<FoodModification>();
    
    public static void registerModification(FoodModification type)
    {
        if (type.item == null) throw new IllegalArgumentException("'item' must be defined for food modification");
        type.toEffect = (ItemStack) ItemUtil.parseStringIntoRecipeItem(type.item, true);
        
        types.add(type);
    }
    
    public ItemStack getStack()
    {
        return toEffect.copy();
    }

    public static FoodModification[] getValues()
    {
        return types.toArray(new FoodModification[] {});
    }
}
