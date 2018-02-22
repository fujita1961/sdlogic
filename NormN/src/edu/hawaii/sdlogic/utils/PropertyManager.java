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
			"roles",
			"valueRoles",
			"lifeSpan",
			"searchIteration",
			"collaborativeRange",
			"friends",
			"periods",
			"repeats",
			"windowSize",
			"drawInterval",
			"animationGIFInterval",
			"printInterval"
	};

	private String[] doubleStrings = {
			"liveCondition",
			"deathRate",
			"outputRate",
			"searchLimit",
			"sigmaOutput",
			"shareRate",
			"exchangeRate"
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
			"enableStoring",
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
			"printExchangeLinksFlag",
			"printStatistics",
			"printEntropyFlag",
			"titleBar",
			"drawMap",
			"drawLinks",
			"drawTightLinks",
			"drawExchange",
			"drawRelation",
			"drawRelationDensity"
	};

	@Deprecated
	public boolean interpreteInt(String key, String val) {
		for(String str: intStrings) {
			if(str.equals(key)) {
				int value = Integer.parseInt(val.trim());

				try {
					Field field = Env.class.getField(key);
					field.set(null, value);
					return true;
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
					return false;
				} catch (IllegalAccessException iae) {
					iae.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	@Deprecated
	public boolean interpreteDouble(String key, String val) {
		for(String str: doubleStrings) {
			if(str.equals(key)) {
				double value = Double.parseDouble(val.trim());

				try {
					Field field = Env.class.getField(key);
					field.set(null, value);
					return true;
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
					return false;
				} catch (IllegalAccessException iae) {
					iae.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	@Deprecated
	public boolean interpreteString(String key, String val) {
		for(String str: stringStrings) {
			if(str.equals(key)) {
				try {
					Field field = Env.class.getField(key);
					field.set(null, val.trim());
					return true;
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
					return false;
				} catch (IllegalAccessException iae) {
					iae.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	@Deprecated
	public boolean interpreteBoolean(String key, String val) {
		for(String str: booleanStrings) {
			if(str.equals(key)) {
				boolean value = Boolean.parseBoolean(val.trim());

				try {
					Field field = Env.class.getField(key);
					field.set(null, value);
					return true;
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
					return false;
				} catch (IllegalAccessException iae) {
					iae.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	public boolean interprete(Properties props) {
		for(Object obj: props.keySet()) {
			String str = (String)obj;
			String val = props.getProperty(str);

			// System.out.println(str + "," + val);

			Class<Env> cls = Env.class;
			try {
				Field field = cls.getField(str);
				Class<?> type = field.getType();

				if(type == int.class) {
					int value = Integer.parseInt(val.trim());
					field.set(null, value);
				} else if(type == double.class) {
					double value = Double.parseDouble(val.trim());
					field.set(null, value);
				} else if(type == boolean.class) {
					String valTrim = val.trim();
					if("true".equals(valTrim)) {
						field.setBoolean(null, true);
					} else if("false".equals(valTrim)) {
						field.setBoolean(null, false);
					} else {
						System.err.printf("key = %s is not a member of properties or value = %s is ilegal.\n", str, val);
						return false;
					}
					/*
					boolean value = Boolean.parseBoolean(val.trim());
					field.set(null, value);
					*/
				} else if(type == String.class) {
					field.set(null, val);
				}

				/*
				if(interpreteInt(str, val) || interpreteDouble(str, val) || interpreteString(str, val)
						|| interpreteBoolean(str, val)) {
				} else {
					System.err.printf("key = %s is not a member of properties or value = %s is ilegal.\n", str, val);
					return false;
				}
				*/
			} catch (NoSuchFieldException e) {
				System.err.printf("key = %s is not a member of properties or value = %s is ilegal.\n", str, val);
				e.printStackTrace();
				return false;
			} catch (IllegalAccessException iae) {
				iae.printStackTrace();
				return false;
			}
		}

		if(Env.printParameterFlag) {
			for(Object obj: props.keySet()) {
				String str = (String)obj;
				String val = props.getProperty(str);

				System.out.println(str + " = " + val);
			}
		}

		return true;
	}
}
