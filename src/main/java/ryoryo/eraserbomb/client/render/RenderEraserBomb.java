package ryoryo.eraserbomb.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import ryoryo.eraserbomb.entity.EntityEraserBomb;

public class RenderEraserBomb extends RenderSnowball<EntityEraserBomb> {
	public RenderEraserBomb(RenderManager renderManager, Item item, RenderItem itemRenderer) {
		super(renderManager, item, itemRenderer);
	}

	// @Override
	// public void doRender(EntityEraserBomb entity, double x, double y, double
	// z, float entityYaw, float partialTicks)
	// {
	// GlStateManager.color(1.0F, 0.0F, 0.0F, 0.0F);
	// super.doRender(entity, x, y, z, entityYaw, partialTicks);
	// }

	// TODO
	@Override
	public ItemStack getStackToRender(EntityEraserBomb entity) {
		return new ItemStack(this.item, 1, entity.getMeta());
	}

	public static class Factory implements IRenderFactory<EntityEraserBomb> {
		private Item item;

		public Factory(Item item) {
			this.item = item;
		}

		@Override
		public Render<? super EntityEraserBomb> createRenderFor(RenderManager manager) {
			return new RenderEraserBomb(manager, this.item, Minecraft.getMinecraft().getRenderItem());
		}
	}
}