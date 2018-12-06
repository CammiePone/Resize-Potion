package com.camellias.resizer.init;

import java.util.ArrayList;
import java.util.List;

import com.camellias.resizer.common.items.ItemBauble;
import com.camellias.resizer.common.items.ItemGrowthBauble;
import com.camellias.resizer.common.items.ItemShrinkingBauble;

import net.minecraft.item.Item;

public class ModItems
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item INERT_BAUBLE = new ItemBauble("inert_bauble");
	public static final Item SHRINKING_BAUBLE = new ItemShrinkingBauble("shrinking_bauble");
	public static final Item GROWTH_BAUBLE = new ItemGrowthBauble("growth_bauble");
}
