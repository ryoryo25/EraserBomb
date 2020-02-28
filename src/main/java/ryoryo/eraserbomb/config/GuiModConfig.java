package ryoryo.eraserbomb.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import ryoryo.eraserbomb.EraserBomb;
import ryoryo.eraserbomb.config.ModConfig.EnumConfigCategory;
import ryoryo.eraserbomb.util.References;

public class GuiModConfig extends GuiConfig
{
	public GuiModConfig(GuiScreen parentScreen)
	{
		super(parentScreen, getConfigElements(), References.MOD_ID, false, false, References.MOD_NAME + " Configurations");
	}

	private static List<IConfigElement> getConfigElements()
	{
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		for(EnumConfigCategory cat : EnumConfigCategory.values())
		{
			EraserBomb.config.getConfig().setCategoryComment(cat.name, cat.comment);
//			for(IConfigElement elem : new ConfigElement(PSV2Core.config.getCofig().getCategory(cat.name)).getChildElements())
//			{
//				list.add(elem);
//			}
			list.add(new ConfigElement(EraserBomb.config.getConfig().getCategory(cat.name)));
		}

		return list;
	}
}