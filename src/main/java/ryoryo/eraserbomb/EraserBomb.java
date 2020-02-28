package ryoryo.eraserbomb;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ryoryo.eraserbomb.config.ModConfig;
import ryoryo.eraserbomb.proxy.CommonProxy;
import ryoryo.eraserbomb.util.References;
import ryoryo.polishedlib.util.Utils;

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, version = References.MOD_VERSION, dependencies = References.MOD_DEPENDENCIES, acceptedMinecraftVersions = References.MOD_ACCEPTED_MC_VERSIONS, useMetadata = true, guiFactory = References.MOD_GUI_FACTORY)
public class EraserBomb
{
	@Instance(References.MOD_ID)
	public static EraserBomb INSTANCE;

	@SidedProxy(clientSide=References.PROXY_CLIENT, serverSide=References.PROXY_COMMON)
	public static CommonProxy proxy;

	public static ModConfig config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		INSTANCE = this;
		Utils.setModId(References.MOD_ID);
		config = new ModConfig(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(config);
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Utils.setModId(References.MOD_ID);
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		Utils.setModId(References.MOD_ID);
		proxy.postInit(event);
	}

	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event)
	{
		Utils.setModId(References.MOD_ID);
		proxy.loadComplete(event);
	}
}