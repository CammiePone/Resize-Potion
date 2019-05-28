package com.camellias.resizer.init;

import java.util.ArrayList;
import java.util.List;

import com.camellias.resizer.common.items.ItemGrowthTrinket;
import com.camellias.resizer.common.items.ItemInertTrinket;
import com.camellias.resizer.common.items.ItemShrinkingTrinket;

import net.minecraft.item.Item;

public class ModItems {
	public static final List<Item> ITEMS = new ArrayList<Item>();

	public static final Item INERT_BAUBLE = new ItemInertTrinket("inert_bauble");
	public static final Item SHRINKING_BAUBLE = new ItemShrinkingTrinket("shrinking_bauble");
	public static final Item GROWTH_BAUBLE = new ItemGrowthTrinket("growth_bauble");
}
