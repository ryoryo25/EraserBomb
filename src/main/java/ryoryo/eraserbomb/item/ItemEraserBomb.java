package ryoryo.eraserbomb.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ryoryo.eraserbomb.entity.EntityEraserBomb;
import ryoryo.eraserbomb.util.References;
import ryoryo.polishedlib.item.ItemBase;
import ryoryo.polishedlib.util.Utils;

public class ItemEraserBomb extends ItemBase implements IItemColor
{
	public static final int[] powers =
	{ 3, 7, 15, 31, 63, 127 };
	public static int types = 3;

	public ItemEraserBomb()
	{
		super("eraser_bomb", CreativeTabs.MISC);
		this.setHasSubtypes(true);
	}

	public static int getType(int damage)
	{
		return damage / 6 + 1;
	}

	public static int getPower(int damage)
	{
		return damage % 6;
	}

	public static int getBombPower(int damage)
	{
		return powers[getPower(damage)];
	}

	public static int size()
	{
		return powers.length * types;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		int power = getPower(stack.getItemDamage());
		tooltip.add(Utils.translatableStringFormatted(References.TOOLTIP_ERASER_BOMB, powers[power], powers[power] * 2 + 1));
	}

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex)
	{
		int damage = stack.getItemDamage();
		int type = getType(damage);
		int power = getPower(damage);
		if(type == 1)
			return 0xffcccc - 0x002222 * power;
		if(type == 2)
			return 0xccccff - 0x222200 * power;
		if(type == 3)
			return 0xffccff - 0x112200 * power;
		if(type == 4)
			return 0xccffcc - 0x220022 * power;
		return 0xffffff;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		int damage = stack.getItemDamage();
		if(damage < 0 || damage >= size())
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);

		if(!Utils.isCreative(player))
			stack.shrink(1);

		world.playSound(player, player.getPosition(), SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if(!world.isRemote)
		{
			if(player.isSneaking())
			{
				//スニークしながら右クリックなら、その場で爆発
				new EntityEraserBomb(world, player, stack).onImpact(null);
			}
			else
			{
				//スニーク無しで右クリックなら、EntityEraserBombをスポーンさせる（投げる）
				world.spawnEntity(new EntityEraserBomb(world, player, stack, 4.5F));
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		int damage = itemStack.getItemDamage();
		return (0 <= damage && damage < size()) ? String.format("%s_%d", getUnlocalizedName(), damage) : getUnlocalizedName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		Utils.registerSubItems(this, size(), tab, items);
	}
}