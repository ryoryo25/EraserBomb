package ryoryo.eraserbomb.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import ryoryo.eraserbomb.config.ModConfig;
import ryoryo.eraserbomb.item.ItemEraserBomb;
import ryoryo.eraserbomb.util.References;
import ryoryo.polishedlib.util.Utils;

public class EntityEraserBomb extends EntityThrowable
{
	//render用
	private int meta;
	//爆弾の種類
	private int bombType;
	//真上投げフラグ
	private boolean isVertical = false;
	//爆発半径。例えば4なら、着弾点を中心に9ｘ9ｘ9の範囲を円形にブロックを消滅させる
	private int bombPower;
	//距離判定用
	private double range;

	public EntityEraserBomb(World world)
	{
		super(world);
	}

	public EntityEraserBomb(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}

	//爆発力、速度を指定してEntityを生成
	public EntityEraserBomb(World world, EntityLivingBase thrower, ItemStack stack, float speed)
	{
		super(world, thrower);
		this.bombPower = ItemEraserBomb.getBombPower(stack.getItemDamage());
		this.bombType = ItemEraserBomb.getType(stack.getItemDamage());
		this.meta = stack.getItemDamage();
		//破壊範囲を+0.6Fにして、半径二乗を計算しておく（+0.6Fにするのは円形を綺麗に見せるため）
		this.range = Math.pow(this.bombPower + 0.6D, 2);
		//投げたプレイヤーの向きに飛んでいくように
		this.shoot(thrower, thrower.rotationPitch, thrower.rotationYaw, 0.0F, speed, 1.0F);
		//投げた方向がほぼ真上なら、真上投げフラグをセット
		this.isVertical = this.motionX < 0.05F && this.motionX > -0.05F && this.motionZ < 0.05F && this.motionZ > -0.05F && this.motionY > 0;
	}

	//爆発力を指定してEntityを生成
	public EntityEraserBomb(World world, EntityLivingBase thrower, ItemStack stack)
	{
		this(world, thrower, stack, 1.5F);
	}

	public int getMeta()
	{
		return this.meta;
	}

	//指定座標で爆発を起こす。ブロックだけを破壊する。
	public void eraseBlocks(BlockPos pos)
	{
		//時間計測用
		long start = System.nanoTime();
		//ブロックは回数カウント用
		int count = 0;

		int px = pos.getX();
		int py = pos.getY();
		int pz = pos.getZ();

		int tx = 0;
		int tz = 0;
		int ty = 0;

		//Warning
		if(this.bombPower > 30)
		{
			Utils.sendChat((EntityPlayer) getThrower(), TextFormatting.RED + "" + TextFormatting.BOLD + Utils.translatableString(References.CHAT_WARNING_ERASER_BOMB1));
			Utils.sendChat((EntityPlayer) getThrower(), Utils.translatableString(References.CHAT_WARNING_ERASER_BOMB2));
		}

		//ブロック破壊
		for(int x = -this.bombPower; x <= this.bombPower; ++x)
		{
			for(int z = -this.bombPower; z <= this.bombPower; ++z)
			{
				for(int y = -this.bombPower; y <= this.bombPower; ++y)
				{
					ty = y + py;
					//爆発範囲外ならスルー
					if(ty < 0 || ty > 255 || x * x + y * y + z * z > range)
						continue;
					tx = x + px;
					tz = z + pz;
					BlockPos posn = new BlockPos(tx, ty, tz);
					//空気ブロックならスルー
					if(this.world.isAirBlock(posn))
						continue;
					//岩盤を判定
					if(this.world.getBlockState(posn) == Blocks.BEDROCK.getDefaultState() && !ModConfig.eraseBedrock)
						continue;
					//ブロックを消滅させ、表示を更新。周囲の情報は更新しない
					this.world.setBlockState(posn, Blocks.AIR.getDefaultState(), 2);
//					this.world.setBlockToAir(posn);
					//					this.worldObj.markBlockNeedsUpdate(tx, ty, tz);
					++count;
				}
			}
		}
		//経過ナノ秒の取得
		long time = System.nanoTime() - start;

		//ブロックカウント設定がtrueなら
		if(ModConfig.isEraseBlockCount)
		{
			//経過時間と破壊ブロック数を表示
			//EntityThrowable.thrower には、投げたキャラ（EntityLiving）のインスタンスが保持されている。
			Utils.sendChat((EntityPlayer) getThrower(), String.format("Erased %d block(s) : (%1.4f sec)", count, time / 1000000000D));
		}
	}

	//モブリストの取得
	protected List<Entity> getEntityList(BlockPos pos, int r)
	{
		double dx = pos.getX();
		double dy = pos.getY();
		double dz = pos.getZ();

		//		this.worldObj.getEntitiesWithinAABB(classEntity, bb)
		//		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBoxFromPool(dx - r, dy - r, dz - r, dx + r, dy + r, dz + r);
		AxisAlignedBB aabb = new AxisAlignedBB(dx, dy, dz, dx, dy, dz).grow(r);
		return this.world.getEntitiesWithinAABBExcludingEntity(getThrower(), aabb);
	}

	//指定座標で爆発を起こす。モブだけを消去する。
	public void killEntities(BlockPos pos)
	{
		//ブロックは回数カウント用
		int count = 0;

		double dx = pos.getX();
		double dy = pos.getY();
		double dz = pos.getZ();

		//モブ消去
		List<Entity> entityList = getEntityList(pos, bombPower);

		for(int i = 0; i < entityList.size(); ++i)
		{
			Entity entity = (Entity) entityList.get(i);
			if(entity instanceof EntityLiving && entity.getDistance(dx, dy, dz) <= range)
			{
				entity.setDead();
				++count;
			}
		}

		//ブロックカウント設定がtrueなら
		if(ModConfig.isEraseBlockCount)
		{
			//モブ消去数を表示
			Utils.sendChat((EntityPlayer) getThrower(), String.format("Killed %d mob(s)", count));
		}
	}

	@Override
	public void onImpact(RayTraceResult result)
	{
		BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
//		BlockPos posp = new BlockPos(getThrower().posX, getThrower().posY, getThrower().posZ);
		//爆発音
		this.world.playSound((EntityPlayer) getThrower(), pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 4.0F, 0.7F);

		//何かにぶつかったら着弾点で爆発
		if((bombType & 1) != 0)
			this.eraseBlocks(pos);
		if((bombType & 2) != 0)
			this.killEntities(pos);
		if(bombType == 4)
		{
			this.eraseBlocks(pos);
			this.createCircle(pos);
		}

		if(!this.world.isRemote)
		{
			//自分自身を消去
			this.setDead();
		}
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if(this.isVertical && getThrower().posY + this.bombPower + 0.6D < this.posY)
		{
			//真上に投げた場合、爆発半径の高さで爆発
			this.posX = thrower.posX;
			this.posY = getThrower().posY + this.bombPower - 1D;
			this.posZ = thrower.posZ;
			this.onImpact((RayTraceResult) null);
		}
	}

	//unused
	public void createCircle(BlockPos pos)
	{
		int cx = pos.getX();
		int cy = pos.getY();
		int cz = pos.getZ();

		int tx = 0;
		int tz = 0;
		int ty = 0;
		double rl = (this.bombPower - 0.4D) * (this.bombPower - 0.4D);
		int rr = 0;

		//外周設置
		for(int x = -this.bombPower; x <= this.bombPower; ++x)
		{
			for(int z = -this.bombPower; z <= this.bombPower; ++z)
			{
				for(int y = -this.bombPower; y <= this.bombPower; ++y)
				{
					ty = y + cy;
					//爆発範囲外ならスルー
					if(ty < 0 || ty > 255)
						continue;
					rr = x * x + y * y + z * z;
					if(rr > range || rr < rl)
						continue;
					tx = x + cx;
					tz = z + cz;
					BlockPos posn = new BlockPos(tx, ty, tz);
					//ブロックを設置、表示を更新。周囲の情報は更新しない
					this.world.setBlockState(posn, Blocks.STONE.getDefaultState(), 2);
//					this.world.update(tx, ty, tz);
				}
			}
		}
	}
}