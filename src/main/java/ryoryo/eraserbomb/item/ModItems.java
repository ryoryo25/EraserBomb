package ryoryo.eraserbomb.item;

import net.minecraft.item.Item;
import ryoryo.eraserbomb.EraserBomb;

public class ModItems {
	public static final Item ITEM_ERASER_BOMB = new ItemEraserBomb();

	public static void init() {
		EraserBomb.REGISTER.registerItem(ITEM_ERASER_BOMB, "eraser_bomb", ItemEraserBomb.size());
	}
}