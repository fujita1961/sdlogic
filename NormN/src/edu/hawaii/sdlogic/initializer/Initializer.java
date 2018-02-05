package edu.hawaii.sdlogic.initializer;

import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.Term;
import edu.hawaii.sdlogic.draw.DrawAny;
import edu.hawaii.sdlogic.draw.DrawPlane;
import edu.hawaii.sdlogic.draw.DrawPlane2;
import edu.hawaii.sdlogic.draw.DrawPlane3;
import edu.hawaii.sdlogic.draw.DrawPlane4;
import edu.hawaii.sdlogic.draw.DrawPlane5;
import edu.hawaii.sdlogic.eval.GDLogicEvaluation;
import edu.hawaii.sdlogic.eval.SDLogicEvaluation;
import edu.hawaii.sdlogic.exchange.MemoryExchange;
import edu.hawaii.sdlogic.exchange.NoExchange;
import edu.hawaii.sdlogic.exchange.SimpleExchange;
import edu.hawaii.sdlogic.fertility.Fertility;
import edu.hawaii.sdlogic.fertility.FlatFertility;
import edu.hawaii.sdlogic.fertility.HoleFertility;
import edu.hawaii.sdlogic.fertility.LinearFertility;
import edu.hawaii.sdlogic.output.CooperativeCalculateOutput;
import edu.hawaii.sdlogic.output.SimpleCalculateOutput;

/**
 * abstract class of Initializer
 * @author fujita
 *
 */
public abstract class Initializer {
	public static void metaInit() {
		if(Env.initClassName == null) {
			Env.initializer = new RandomInitializer();
		} else if(Env.initClassName.equals("BestSelectInitializer")) {
			Env.initializer = new BestSelectInitializer(50, 100, 50, 3);
		} else if(Env.initClassName.equals("CenterCircleInitializer")) {
			Env.initializer = new CenterCircleInitializer(5);
		} else if(Env.initClassName.equals("CenterSquareInitializer")) {
			Env.initializer = new CenterSquareInitializer(5);
		} else if(Env.initClassName.equals("DenseInitializer")) {
			Env.initializer = new DenseInitializer(7);
		} else if(Env.initClassName.equals("RandomInitializer")) {
			Env.initializer = new RandomInitializer();
		} else if(Env.initClassName.startsWith("FileInitializer")) {
			int start = Env.initClassName.indexOf("(");
			int end = Env.initClassName.indexOf(")");
			String fileName = Env.initClassName.substring(start + 1, end);
			Env.initializer = new FileInitializer(fileName);
		} else {
			Env.initializer = new RandomInitializer();
		}
	}

	protected static void initTypes() {
		if(Env.draw == null) {
			if(Env.drawClassName == null) {
				Env.draw = new DrawPlane();
			} else if(Env.drawClassName.equals("DrawPlane")) {
				Env.draw = new DrawPlane();
			} else if(Env.drawClassName.equals("DrawPlane2")) {
				Env.draw = new DrawPlane2();
			} else if(Env.drawClassName.equals("DrawPlane3")) {
				Env.draw = new DrawPlane3();
			} else if(Env.drawClassName.equals("DrawPlane4")) {
				Env.draw = new DrawPlane4();
			} else if(Env.drawClassName.equals("DrawPlane5")) {
				Env.draw = new DrawPlane5();
			} else if(Env.drawClassName.equals("DrawAny")) {
				Env.draw = new DrawAny();
			} else {
				Env.draw = new DrawPlane();
			}

			Env.draw.init();
		}

		if(Env.evalLogicName == null) {
			Env.eval = new SDLogicEvaluation();
		} else if(Env.evalLogicName.equals("SDLogic")) {
			Env.eval = new SDLogicEvaluation();
		} else if(Env.evalLogicName.equals("GDLogic")) {
			Env.eval = new GDLogicEvaluation();
		} else {
			Env.eval = new SDLogicEvaluation();
		}

		if(Env.roleNames == null) {
			int types = Env.roles;

			if(Env.enableExchanging) {
				types++;
			}

			if(Env.enableCollaborating) {
				types++;
			}

			Env.roleNames = new String[types];
			for(int i = 0; i < Env.roles; i++) {
				Env.roleNames[i] = ("Role-" + i).intern();
			}

			if(Env.enableExchanging) {
				Env.roleNames[Env.roles] = Term.EXCHANGING;
			}

			if(Env.enableCollaborating) {
				Env.roleNames[Env.roles + 1] = Term.COLLABORATING;
			}
		}
	}

	/**
	 * common method for initializing fertility maps and output functions.
	 */
	protected static void initFertilityAndOutputFunction() {
		if(Env.fertilityMaps == null) {
			Env.fertilityMaps = new double[Env.roles][Env.mapWidth][Env.mapHeight];
		}

		if(Env.fertilityClasses == null) {
			Env.fertilityClasses = new Fertility[Env.roles];

			for(int i = 0; i < Env.roles; i++) {
				if(Env.fertilityClassName == null) {
					Env.fertilityClasses[i] = FlatFertility.get();
				} else if(Env.fertilityClassName.equals("HoleFertility")) {
					Env.fertilityClasses[i] = HoleFertility.get(3.0, 1.0, 10.0, 5.0);
				} else if(Env.fertilityClassName.equals("LinearFertility")) {
					Env.fertilityClasses[i] = LinearFertility.get(0.6, 0.5 / Env.roles + ((double)i) / Env.roles);
				} else {
					Env.fertilityClasses[i] = FlatFertility.get();
				}
			}

			for(int i = 0; i < Env.roles; i++) {
				for(int x = 0; x < Env.mapWidth; x++) {
					for(int y = 0; y < Env.mapHeight; y++) {
						Env.fertilityMaps[i][x][y] = Env.fertilityClasses[i].calculate(x, y);
					}
				}
			}
		}

		if(Env.output == null) {
			if(Env.outputClassName == null) {
				Env.output = new SimpleCalculateOutput();
			} else if(Env.outputClassName.equals("CooperativeCalculateOutput")) {
				Env.output = new CooperativeCalculateOutput();
			} else {
				Env.output = new SimpleCalculateOutput();
			}
		}

		if(Env.exchangeClassName == null) {
			Env.exchange = new SimpleExchange();
		} else if(Env.exchangeClassName.equals("NoExchange")) {
			Env.exchange = new NoExchange();
		} else if(Env.exchangeClassName.equals("MemoryExchange")) {
			Env.exchange = new MemoryExchange();
		} else {
			Env.exchange = new SimpleExchange();
		}
	}

	public abstract void init();
}
