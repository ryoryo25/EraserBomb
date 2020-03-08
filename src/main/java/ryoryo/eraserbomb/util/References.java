package ryoryo.eraserbomb.util;

public class References
{
	public static final String MOD_ID = "eraserbomb";
	public static final String MOD_NAME = "EraserBomb";

	public static final String MOD_VERSION_MAJOR = "GRADLE.VERSION_MAJOR";
	public static final String MOD_VERSION_MINOR = "GRADLE.VERSION_MINOR";
	public static final String MOD_VERSION_PATCH = "GRADLE.VERSION_PATCH";
	public static final String MOD_VERSION = MOD_VERSION_MAJOR + "." + MOD_VERSION_MINOR + "." + MOD_VERSION_PATCH;

	public static final String MOD_DEPENDENCIES = "required-after:forge@[14.23.4.2705,);"
//												+ "required-after:polishedlib@[1.0.0,);";
												+ "required-after:polishedlib;";

	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.12.2]";
	public static final String MOD_GUI_FACTORY = "ryoryo.eraserbomb.config.GuiFactoryModConfig";

	public static final String PROXY_CLIENT = "ryoryo.eraserbomb.proxy.ClientProxy";
	public static final String PROXY_COMMON = "ryoryo.eraserbomb.proxy.CommonProxy";

	//Entity Id of eraser bomb
	public static final int ENTITY_ID_ERASER_BOMB = 0;

	//Eraser Bombのツールチップの表示
	public static final String TOOLTIP_ERASER_BOMB = "tooltip.eraser_bomb";
	//Eraser Bombの警告
	public static final String CHAT_WARNING_ERASER_BOMB1 = "chat.warning_eraser_bomb1";
	public static final String CHAT_WARNING_ERASER_BOMB2 = "chat.warning_eraser_bomb2";
}