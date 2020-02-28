package ryoryo.eraserbomb.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ryoryo.eraserbomb.client.render.RenderEraserBomb;
import ryoryo.eraserbomb.entity.EntityEraserBomb;
import ryoryo.eraserbomb.item.ItemEraserBomb;
import ryoryo.eraserbomb.item.ModItems;
import ryoryo.polishedlib.util.Utils;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		Utils.registerEntityRendering(EntityEraserBomb.class, new RenderEraserBomb.Factory(ModItems.ITEM_ERASER_BOMB));
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		Utils.registerItemColor(new ItemEraserBomb(), ModItems.ITEM_ERASER_BOMB);
	}
}