package edu.hawaii.sdlogic.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import edu.hawaii.sdlogic.Env;

public class PropertyManager {
	public Properties read(String file) {
		Properties props = new Properties();
		try {
			InputStream is = new FileInputStream(file);
			props.load(is);
			is.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return props;
	}

	public void write(Properties props, String file) {
		try {
			OutputStream os = new FileOutputStream(file);
			props.store(os, "");
			os.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private String[] intStrings = {
			"displayWidth",
			"displayHeight",
			"displayMargin",
			"cellWidth",
			"cellHeight",
			"mapWidth",
			"mapHeight",
			"actors",
			"types",
			"lifeSpan",
			"searchIteration",
			"collaborativeRange",
			"friends",
			"loops",
			"windowSize",
			"drawInterval",
			"animationGIFInterval"
	};

	private String[] doubleStrings = {
			"liveCondition",
			"deathRate",
			"outputRate",
			"searchLimit",
			"sigmaOutput"
	};

	private String[] stringStrings = {
			"fertilityClassName",
			"outputClassName",
			"evalLogicName",
			"initClassName",
			"drawClassName",
			"animationGIFFileName",
			"exchangeClassName"
	};

	private String[] booleanStrings = {
			"enableExchanging",
			"enableCollaborating",
			"changePopulation",
			"survive",
			"friendFlag",
			"learnFlag",
			"macroFlag1",
			"macroFlag2",
			"macroFlag3",
			"variableCapability",
			"printCollaborationCountFlag",
			"printSkillLevelsFlag",
			"printStatistics",
			"printEntropyFlag",
			"titleBar"
	};

	public void interpreteInt(Properties props) {
		for(String key: intStrings) {
			String str = props.getProperty(key);
			if(str != null) {
				int value = Integer.parseInt(str.trim());

				try {
					Field field = Env.class.getField(key);
					field.set(null, value);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException iae) {
					iae.printStackTrace();
				}
			}
		}
	}

	public void interpreteDouble(Properties props) {
		for(String key: doubleStrings) {
			String str = props.getProperty(key);
			if(str != null) {
				double value = Double.parseDouble(str.trim());

				try {
					Field field = Env.class.getField(key);
					field.set(null, value);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException iae) {
					iae.printStackTrace();
				}
			}
		}
	}

	public void interpreteString(Properties props) {
		for(String key: stringStrings) {
			String str = props.getProperty(key);
			if(str != null) {
				try {
					Field field = Env.class.getField(key);
					field.set(null, str.trim());
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException iae) {
					iae.printStackTrace();
				}
			}
		}
	}

	public void interpreteBoolean(Properties props) {
		for(String key: booleanStrings) {
			String str = props.getProperty(key);
			if(str != null) {
				boolean value = Boolean.parseBoolean(str.trim());

				try {
					Field field = Env.class.getField(key);
					field.set(null, value);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException iae) {
					iae.printStackTrace();
				}
			}
		}
	}

	public void interprete(Properties props) {
		interpreteInt(props);
		interpreteDouble(props);
		interpreteString(props);
		interpreteBoolean(props);
	}
}
