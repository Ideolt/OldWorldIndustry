package oldworldindustry.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

public class Config
{

	// ------------- File paths --------------

	private static String DIR_PATH = "./config/OWI";
	private static String CONFIGS = "./config/OWI/OWIConfig.properties";

	// ------------- Properties ------------

	public static int LEVEL_RANGE_START;

	// ------------- Functions -------------

	public static void loadConfigs()
	{
		File Directory = new File(DIR_PATH);
		File ConfigFile = new File(CONFIGS);

		if (!Directory.exists())
			Directory.mkdir();

		if (!ConfigFile.exists())
		{
			try
			{
				ConfigFile.createNewFile();
				writeConfig(ConfigFile);
				readConfigs(ConfigFile);
			} catch (IOException e)
			{
				System.out.println("OWI ConfigHandler File Creation Error: "
						+ e.getMessage());
			}
		} else
		{
			readConfigs(ConfigFile);
		}

	}

	public static void writeConfig(File ConfigFile)
	{
		try
		{
			PrintWriter fw = new PrintWriter(ConfigFile);

			fw.println("# ---- OWI Configs ---- ");
			fw.println("# Version 0.1 ");
			fw.println("");
			fw.println("#The starting point of Ids in OWI (change only if there are conflicting mods)");
			fw.println("LevelRangeStart = 500");
			fw.close();
		} catch (Exception e)
		{

			System.out.println("OWI ConfigHandler Write error: "
					+ e.getMessage());
		}

	}

	public static void readConfigs(File ConfigFile)
	{
		try
		{

			Properties OWIProperties = new Properties();
			InputStream is = new FileInputStream(ConfigFile);
			OWIProperties.load(is);
			is.close();

			// ------------ Configuration ------------

			
			LEVEL_RANGE_START = Integer.parseInt(OWIProperties.getProperty("LevelRangeStart", "500"));
		
		} catch (Exception e)
		{

			System.out.println("AH ConfigHandler Config read error: "
					+ e.getMessage());

		}

	}

}
