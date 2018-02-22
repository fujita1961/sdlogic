package edu.hawaii.sdlogic;

import java.util.List;
import java.util.Random;

import edu.hawaii.sdlogic.draw.Draw;
import edu.hawaii.sdlogic.eval.Evaluation;
import edu.hawaii.sdlogic.exchange.Exchange;
import edu.hawaii.sdlogic.exchange.SimpleExchange;
import edu.hawaii.sdlogic.fertility.Fertility;
import edu.hawaii.sdlogic.initializer.Initializer;
import edu.hawaii.sdlogic.output.CalculateOutput;
import edu.hawaii.sdlogic.utils.Entropy;

/**
 * Environmental parameter class
 * @author fujita
 *
 */
public class Env {
	// display parameters

	/**
	 * display width
	 */
	public static int displayWidth;

	/**
	 * display height
	 */
	public static int displayHeight;

	/**
	 * margin around the display
	 */
	public static int displayMargin;

	/**
	 * one cell width
	 */
	public static int cellWidth;

	/**
	 * one cell height
	 */
	public static int cellHeight;


	// map parameters

	/**
	 * map width
	 */
	public static int mapWidth;

	/**
	 * map height
	 */
	public static int mapHeight;

	/**
	 * actor array on map
	 */
	public static Actor[][] map;


	// actor parameters

	/**
	 * the initial number of actors
	 */
	public static int actors;

	/**
	 * active actor list
	 */
	public static List<Actor> actorList;

	// operant resources

	/**
	 * number of critical operant resources
	 */
	public static int roles = 2;

	/**
	 * number of stocking resources
	 */
	public static int stockRoles = 0;

	/**
	 * names of operant resources
	 * The length of this array is more than Env.types,
	 * because this array involves Exchanging and Collaborating operant resources.
	 */
	public static String[] roleNames;

	/**
	 * enable exchanging operant resource
	 */
	public static boolean enableExchanging = false;

	/**
	 * enable collaborating operant resource
	 */
	public static boolean enableCollaborating = false;

	/**
	 * enable storing operant resource
	 */
	public static boolean enableStoring = false;

	/**
	 * enable exchanging operant resource
	 */
	public static boolean enableStoring2 = false;

	/**
	 * enable exchanging operant resource
	 */
	public static double storeRate = 4.0;

	/**
	 * reduction rate for stock
	 */
	public static double reductionRate = 1.0;

	/**
	 * share rate for share cooperative mode
	 */
	public static double shareRate = 0;

	// land fertility

	/**
	 * fertility map
	 * The dimensions are types, x and y.
	 */
	public static double[][][] fertilityMaps;

	/**
	 * class array for fertiflity calculation.
	 * The array dimension is the number of types.
	 */
	public static Fertility[] fertilityClasses;

	/**
	 * Fertility Class Name
	 */
	public static String fertilityClassName = null;


	// parameters for evolutionary mechanisms

	/**
	 * actor's life span
	 */
	public static int lifeSpan = 80;

	/**
	 * the minimum amount of operand resources for surviving
	 */
	public static double liveCondition = 1.0;

	/*
	 * There are three types of evolutionary mechanisms.
	 *   survive mode: Population does not change. The actors that do not satisfy the condition imidiately die,
	 *    and new actors are regenerated.
	 *   death rate mode: Population does not change. The poor actors are selected based on the deathRate ratio,
	 *    and they are regenarated.
	 *   change population: Population changes. The actors that do not satisfy the condition imidiately die,
	 *    and children are periodically born from alive actors.
	 */

	/**
	 * change population mode
	 */
	public static boolean changePopulation = false;

	/**
	 * survive mode
	 */
	public static boolean survive = true;

	/**
	 * If both changePopulation and survive are false, death rate mode is set,
	 * and then the deathRate parameter becomes active.
	 */
	public static double deathRate = 0.1;

	/**
	 * control the output level.
	 */
	public static double outputRate = 1.0;

	// exchange parameters

	/**
	 * exchange rate
	 */
	public static double exchangeRate = 0;

	/**
	 * the limit of the itereation number for finding exchange partners
	 */
	public static int searchIteration = 50;

	/**
	 * the maximum distance for sarching exchange partners.
	 */
	public static double searchLimit = 200;

	// collaboration parameters

	/**
	 * friendFlag is a flag for memorizing former collaboration partners.
	 */
	public static boolean friendFlag = true;

	/**
	 * The range for finding collaborating partners
	 */
	public static int collaborativeRange = 1;

	/**
	 * The number of collaboration partners.
	 */
	public static int friends = 2;

	// other parameters

	/**
	 * learning flag
	 * In most cases, the flag should be false.
	 */
	public static boolean learnFlag = false;

	/**
	 * macro pattern recognition
	 */
	public static boolean macroFlag1 = false;
	public static boolean macroFlag2 = false;
	public static boolean macroFlag3 = false;

	/**
	 * fluctuation of output
	 */
	public static double sigmaOutput = 0.05;

	/**
	 * determines the initial values of skill levels should be fix or randomly assigned.
	 */
	public static boolean variableCapability = false;

	/**
	 * the number of simulation periods
	 */
	public static int periods = 1000;

	/**
	 * the number of simulation periods
	 */
	public static boolean stopAtMax = false;

	/**
	 * the number of repeticion for executing simulations
	 */
	public static int repeats = 1;

	// public static boolean haveCollaboratingOperantResource = false;

	/**
	 * specify output calculation functions.
	 */
	public static CalculateOutput output;


	/**
	 * Output Class Name
	 */
	public static String outputClassName = null;

	/**
	 * S-D logic or G-D logic
	 */
	public static Evaluation eval = null;

	/**
	 * eval logic
	 */
	public static String evalLogicName = "SDLogic";

	/**
	 * exchange method
	 */
	public static Exchange exchange = new SimpleExchange();

	/**
	 *
	 */
	public static String exchangeClassName = "SimpleExchange";

	/**
	 * initializer
	 */
	public static Initializer initializer = null;

	/**
	 * initializer class name
	 */
	public static String initClassName = null;

	/**
	 * window size for search
	 */
	public static int windowSize = 5;

	/**
	 * common random seed
	 */
	public static Random rand;

	/**
	 * drawing class
	 */
	public static Draw draw;

	/**
	 * Draw Class Name
	 */
	public static String drawClassName = null;

	/**
	 * drawing interval
	 */
	public static int drawInterval = 1;

	/**
	 * Draw Map
	 */
	public static boolean drawMap = false;

	/**
	 * Draw Links
	 */
	public static boolean drawLinks = false;

	/**
	 * Draw TightLinks
	 */
	public static boolean drawTightLinks = false;

	/**
	 * Draw TightLinks
	 */
	public static boolean drawExchange = false;

	/**
	 * Draw Relation
	 */
	public static boolean drawRelation = false;

	/**
	 * Draw Relation Density
	 */
	public static boolean drawRelationDensity = false;


	/**
	 * interval for animationGIF
	 */
	public static int animationGIFInterval = 20;

	/**
	 * file name of animationGIF
	 */
	public static String animationGIFFileName = null;

	/**
	 * print interval
	 */
	public static boolean printParameterFlag = true;

	/**
	 * print interval
	 */
	public static int printInterval = 1;

	/**
	 * print the number of collaboration friends
	 */
	public static boolean printCollaborationCountFlag = false;

	/**
	 * print skill levels for each role
	 */
	public static boolean printSkillLevelsFlag = false;

	/**
	 * print exchange links
	 */
	public static boolean printExchangeLinksFlag = false;

	/**
	 * print statistics
	 */
	public static boolean printStatistics = false;

	/**
	 * print entropy information
	 */
	public static boolean printEntropyFlag = false;

	/**
	 * title bar on/off
	 */
	public static boolean titleBar = true;

	/**
	 * entropy class
	 */
	public static Entropy entropy = new Entropy();

	public static boolean DEBUG = false;

	public static double EPSILON = 0.000001;
}
