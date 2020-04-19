package ryoryo.eraserbomb.proxy;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ryoryo.eraserbomb.EraserBomb;
import ryoryo.eraserbomb.entity.EntityEraserBomb;
import ryoryo.eraserbomb.item.ModItems;
import ryoryo.eraserbomb.util.References;
import ryoryo.polishedlib.util.Utils;
import ryoryo.polishedlib.util.enums.EnumColor;

public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		ModItems.init();
		EraserBomb.REGISTER.registerModEntity(EntityEraserBomb.class, "eraser_bomb", References.ENTITY_ID_ERASER_BOMB, EraserBomb.INSTANCE, 32, 5, true);
	}

	public void init(FMLInitializationEvent event)
	{
		for(int i = 0; i < 18; i ++)
		{
			if(i == 0)
				addShapelessRecipe("eraser_bomb_" + i, new ItemStack(ModItems.ITEM_ERASER_BOMB, 1, i), Items.SNOWBALL, EnumColor.RED.getDyeOreName(), Blocks.TNT);
			else if(Utils.isInAToB(i, 1, 5) || Utils.isInAToB(i, 7, 11))
				addShapelessRecipe("eraser_bomb_" + i, new ItemStack(ModItems.ITEM_ERASER_BOMB, 1, i), new ItemStack(ModItems.ITEM_ERASER_BOMB, 1, i - 1), new ItemStack(ModItems.ITEM_ERASER_BOMB, 1, i - 1));
			else if(i == 6)
				addShapelessRecipe("eraser_bomb_" + i, new ItemStack(ModItems.ITEM_ERASER_BOMB, 1, i), Items.SNOWBALL, EnumColor.BLUE.getDyeOreName(), Blocks.TNT);
			else if(Utils.isInAToB(i, 12, 17))
				addShapelessRecipe("eraser_bomb_" + i, new ItemStack(ModItems.ITEM_ERASER_BOMB, 1, i), new ItemStack(ModItems.ITEM_ERASER_BOMB, 1, i - 12), new ItemStack(ModItems.ITEM_ERASER_BOMB, 1, i - 6));
		}
	}

	public void postInit(FMLPostInitializationEvent event)
	{
	}

	public void loadComplete(FMLLoadCompleteEvent event)
	{
	}

	public static void addShapelessRecipe(String name, @Nonnull ItemStack output, Object... params)
	{
		EraserBomb.REGISTER.addShapelessRecipe(name, output, params);
	}
}