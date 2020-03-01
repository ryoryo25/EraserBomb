package ryoryo.eraserbomb.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ryoryo.eraserbomb.EraserBomb;
import ryoryo.eraserbomb.util.References;

//@Config(modid = LibMisc.MOD_ID)
public class ModConfig
{
	private static Configuration config;

	public ModConfig(File configFile)
	{
		config = new Configuration(configFile);
		loadConfigs();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
	{
		if(eventArgs.getModID().equals(References.MOD_ID))
			loadConfigs();
	}

	private void loadConfigs()
	{
		try
		{
			generalConfigs(EnumConfigCategory.GENERAL.getName());
		}
		catch(Exception e)
		{
			EraserBomb.logger.addError("Error loading config.");
		}
		finally
		{
			if(config.hasChanged())
				config.save();
		}
	}

	public Configuration getConfig()
	{
		return config;
	}

	//General--------------------------------------------------------------------------------------
	public static boolean isEraseBlockCount;
	public static boolean eraseBedrock;

	public void generalConfigs(String general)
	{
		isEraseBlockCount = config.getBoolean("IsBlockCount", general, false, "Display the number of erase blocks at the time of explosion in the chat field.");
		eraseBedrock = config.getBoolean("EraseBedrock", general, true, "Wheather Eraser Bombs erase Bedrocks.");
	}

	public static enum EnumConfigCategory
	{
		GENERAL("general", "General Settings"),;

		public final String name;
		public final String comment;

		EnumConfigCategory(String name, String comment)
		{
			this.name = name;
			this.comment = comment;
		}

		public String getName()
		{
			return this.name;
		}

		public String getComment()
		{
			return this.comment;
		}

		public static int getLength()
		{
			return EnumConfigCategory.values().length;
		}
	}
}
