package edu.hawaii.sdlogic;

import java.util.Properties;

import edu.hawaii.sdlogic.fertility.FlatFertility;
import edu.hawaii.sdlogic.utils.Loop;
import edu.hawaii.sdlogic.utils.PropertyManager;

public class Main {
	/**
	 * dummy function for loading important clesses on memory
	 */
	public static void dummy() {
		FlatFertility.get();
	}


	public static void test1() {
		Env.displayMargin = 10;
		Env.cellWidth = 10;
		Env.cellHeight = 10;
		Env.mapWidth = 100;
		Env.mapHeight = 100;
		Env.actors = 2000;
		Env.drawClassName = "DrawPlane2";

		Env.outputRate = 1.4;
		// Env.outputRate = 1.3;
		Env.searchIteration = 100;
		Env.searchLimit = 2000;
		Env.learnFlag = false;;

		Env.enableExchanging = true;
		Env.outputClassName = "CooperativeCalculateOutput";

		// Env.eval = new GDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = true;
		Env.friends = 2;
		Env.collaborativeRange = 3;
		Env.windowSize = 3;

		Env.initClassName = "RandomInitializer";

		Loop.mainLoop();
	}

	public static void test2() {
		Env.displayMargin = 10;

		Env.cellWidth = 50;
		Env.cellHeight = 50;
		Env.mapWidth = 20;
		Env.mapHeight = 20;
		Env.actors = 300;

		Env.drawClassName = "DrawPlane";
		Env.outputRate = 1.4;
		Env.searchIteration = 10;
		Env.searchLimit = 200;
		Env.learnFlag = false;

		Env.enableExchanging = true;

		Env.outputClassName = "CooperativeCalculateOutput";

		// Env.eval = new GDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = true;
		Env.friends = 2;
		Env.collaborativeRange = 3;

		Env.initClassName = "RandomInitializer";

		Loop.mainLoop();
	}

	public static void test3() {
		Env.displayMargin = 10;
		/*
		Env.cellWidth = 50;
		Env.cellHeight = 50;
		Env.mapWidth = 20;
		Env.mapHeight = 20;
		Env.actors = 300;
		*/
		Env.cellWidth = 5;
		Env.cellHeight = 5;
		Env.mapWidth = 200;
		Env.mapHeight = 200;
		Env.actors = 8000;

		Env.roles = 2;
		Env.enableExchanging = true;

		Env.drawClassName = "DrawPlane";
		Env.outputRate = 0.7 * Env.roles;
		Env.searchIteration = 100;
		Env.searchLimit = 1000;
		Env.learnFlag = false;

		Env.outputClassName = "CooperativeCalculateOutput";

		// Env.eval = new GDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = true;
		Env.friends = 8;
		Env.collaborativeRange = 3;

		Env.initClassName = "RandomInitializer";

		Loop.mainLoop();
	}

	public static void test4() {
		Env.displayMargin = 10;

		Env.cellWidth = 5;
		Env.cellHeight = 5;
		Env.mapWidth = 200;
		Env.mapHeight = 200;
		Env.actors = 10000;

		Env.roles = 5;
		Env.enableExchanging = true;

		Env.drawClassName = "DrawPlane";

		// 0.38 0.39-0.41 0.42-0.5
		Env.outputRate = 0.80 * Env.roles;
		Env.searchIteration = 100;
		Env.searchLimit = 2000;
		Env.learnFlag = true;

		Env.outputClassName = "CooperativeCalculateOutput";

		// Env.eval = new GDLogicEvaluation();
		// Env.eval = new SDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = true;
		Env.friends = 8;
		Env.collaborativeRange = 2;
		Env.macroFlag1 = false;

		Env.initClassName = "RandomInitializer";

		Loop.mainLoop();
	}

	public static void test5() {
		Env.displayMargin = 10;

		Env.cellWidth = 5;
		Env.cellHeight = 5;
		Env.mapWidth = 20;
		Env.mapHeight = 20;
		Env.actors = 210;

		Env.roles = 2;
		Env.enableExchanging = true;

		Env.drawClassName = "DrawPlane2";

		Env.outputRate = 0.60 * Env.roles;
		Env.searchIteration = 100;
		Env.searchLimit = 800;
		Env.learnFlag = false;

		Env.outputClassName = "CooperativeCalculateOutput";

		// Env.eval = new GDLogicEvaluation();
		// Env.eval = new SDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = true;
		Env.friends = 3;
		Env.collaborativeRange = 3;
		Env.macroFlag1 = false;

		Env.initClassName = "RandomInitializer";

		Loop.mainLoop();
	}


	// change population
	public static void test6() {
		Env.displayMargin = 10;

		Env.cellWidth = 5;
		Env.cellHeight = 5;
		Env.mapWidth = 200;
		Env.mapHeight = 200;
		Env.actors = 350;

		Env.roles = 2;
		Env.enableExchanging = true;
		Env.enableCollaborating = true;

		Env.drawClassName = "DrawPlane2";

		Env.outputRate = 0.9 * Env.roles;
		Env.searchIteration = 100;
		Env.searchLimit = 2000;
		Env.learnFlag = false;

		Env.outputClassName = "CooperativeCalculateOutput";

		// Env.eval = new GDLogicEvaluation();
		// Env.eval = new SDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = true;
		Env.friends = 6;
		Env.collaborativeRange = 2;
		Env.macroFlag1 = false;

		Env.changePopulation = true;
		Env.windowSize = 3;

		Env.initClassName = "DenseInitializer";

		Loop.mainLoop();
	}

	public static void test7() {
		Env.displayMargin = 10;

		Env.cellWidth = 5;
		Env.cellHeight = 5;
		Env.mapWidth = 200;
		Env.mapHeight = 200;
		Env.actors = 10000;

		Env.roles = 5;
		Env.enableExchanging = true;
		Env.enableCollaborating = true;

		Env.drawClassName = "DrawPlane";

		Env.outputRate = 0.8 * Env.roles;
		Env.searchIteration = 100;
		Env.searchLimit = 2000;
		Env.learnFlag = false;

		Env.outputClassName = "CooperativeCalculateOutput";

		// Env.eval = new GDLogicEvaluation();
		// Env.eval = new SDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = true;
		Env.friends = 3;
		Env.collaborativeRange = 3;
		Env.windowSize = 2;
		Env.macroFlag1 = false;
		// Env.haveCollaboratingOperantResource = true;

		Env.initClassName = "RandomInitializer";

		Loop.mainLoop();
	}

	// no collaboration
	public static void test10() {
		Env.displayMargin = 10;

		Env.cellWidth = 5;
		Env.cellHeight = 5;
		Env.mapWidth = 200;
		Env.mapHeight = 200;
		Env.actors = 10000;

		Env.roles = 2;
		Env.enableExchanging = true;
		Env.enableCollaborating = true;

		Env.drawClassName = "DrawPlane2";

		//Env.outputRate = 1.10 * Env.types;
		Env.outputRate = 0.90 * Env.roles;
		Env.searchIteration = 100;
		Env.searchLimit = 2000;
		Env.learnFlag = false;

		Env.fertilityClassName = "LinearFertility";
		Env.outputClassName = null;

		// Env.eval = new GDLogicEvaluation();
		// Env.eval = new SDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = false;
		Env.friends = 3;
		Env.collaborativeRange = 3;
		Env.windowSize = 3;
		Env.macroFlag1 = false;
		// Env.haveCollaboratingOperantResource = true;
		// Env.exchange = new NoExchange();

		Loop.mainLoop();
	}

	// collaboration
	public static void test11() {
		Env.displayMargin = 10;

		Env.cellWidth = 5;
		Env.cellHeight = 5;
		Env.mapWidth = 200;
		Env.mapHeight = 200;

		Env.actors = 10000; // 2000-20000

		Env.roles = 2;
		Env.enableExchanging = true;
		Env.enableCollaborating = true;

		Env.drawClassName = "DrawPlane";

		Env.outputRate = 0.625 * Env.roles; // 0.6-0.90
		// Env.outputRate = 0.66 * Env.types; // 0.6-0.90
		// Env.lifeSpan = 2000;
		Env.searchIteration = 100;
		Env.searchLimit = 2000;
		Env.learnFlag = false;

		Env.fertilityClassName = null;
		Env.outputClassName = "CooperativeCalculateOutput";

		// Env.eval = new GDLogicEvaluation();
		// Env.eval = new SDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = true;
		Env.friends = 5;
		Env.collaborativeRange = 3;
		Env.windowSize = 3;
		Env.macroFlag1 = false;
		// Env.haveCollaboratingOperantResource = true;

		Env.initClassName = "RandomInitializer";

		Loop.mainLoop();
	}

	// macro
	public static void test12() {
		Env.displayMargin = 10;

		Env.cellWidth = 5;
		Env.cellHeight = 5;
		Env.mapWidth = 200;
		Env.mapHeight = 200;

		Env.actors = 10000; // 2000-20000

		Env.roles = 2;
		Env.enableExchanging = true;
		Env.enableCollaborating = true;

		Env.drawClassName = "DrawPlane2";

		Env.outputRate = 0.64 * Env.roles; // 0.6-0.90
		Env.searchIteration = 100;
		Env.searchLimit = 2000;
		Env.learnFlag = false;

		Env.fertilityClassName = null;
		Env.outputClassName = "CooperativeCalculateOutput";

		// Env.eval = new GDLogicEvaluation();
		// Env.eval = new SDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = true;
		Env.friends = 5;
		Env.collaborativeRange = 3;
		Env.windowSize = 3;
		Env.macroFlag1 = true;
		// Env.haveCollaboratingOperantResource = true;

		Env.initClassName = "RandomInitializer";

		Loop.mainLoop();
	}

	// change population
	public static void test16() {
		Env.displayMargin = 10;

		Env.cellWidth = 5;
		Env.cellHeight = 5;
		Env.mapWidth = 200;
		Env.mapHeight = 200;
		Env.actors = 350;

		Env.roles = 2;
		Env.enableExchanging = true;
		Env.enableCollaborating = true;

		Env.drawClassName = "DrawPlane";
		Env.outputRate = 0.60 * Env.roles;
		Env.searchIteration = 100;
		Env.searchLimit = 2000;
		Env.learnFlag = false;

		Env.fertilityClassName = null;
		Env.outputClassName = "CooperativeCalculateOutput";

		// Env.eval = new GDLogicEvaluation();
		// Env.eval = new SDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = true;
		Env.friends = 6;
		// Env.friends = 10;
		Env.collaborativeRange = 2;
		// Env.cooperativeRange = 6;
		Env.macroFlag1 = false;

		Env.changePopulation = true;
		Env.windowSize = 3;

		Env.variableCapability = true;

		// Env.initializer = new BestSelectInitializer(50, 100, 50, 3);
		// Env.initializer = new BestSelectInitializer(100, 100, 50, 3);
		// Env.initializer = new BestSelectInitializer(20, 20, 20, 3);

		Env.initClassName = "FileInitializer(c:/home/doc/UH/data/data100-12.txt)";

		Loop.mainLoop();
	}

	// change population
	public static void test17() {
		Env.displayMargin = 0;

		Env.cellWidth = 1;
		Env.cellHeight = 1;
		Env.mapWidth = 200;
		Env.mapHeight = 200;

		/*
		Env.displayMargin = 10;

		Env.cellWidth = 25;
		Env.cellHeight = 25;
		Env.mapWidth = 40;
		Env.mapHeight = 40;
		*/

		Env.actors = 50;

		Env.roles = 2;
		Env.enableExchanging = true;
		Env.enableCollaborating = true;

		Env.drawClassName = "DrawPlane2";
		Env.titleBar = true;
		Env.outputRate = 0.70 * Env.roles;
		Env.searchIteration = 100;
		Env.searchLimit = 2000;
		Env.learnFlag = false;

		Env.fertilityClassName = "HoleFertility";
		Env.outputClassName = "CooperativeCalculateOutput";

		// Env.eval = new GDLogicEvaluation();
		// Env.eval = new SDLogicEvaluation();
		Env.evalLogicName = "SDLogic";

		Env.survive = true;
		Env.friendFlag = true;
		Env.friends = 6;
		// Env.friends = 20;
		Env.collaborativeRange = 2;
		// Env.cooperativeRange = 10;
		Env.macroFlag1 = false;
		Env.macroFlag3 = false;

		Env.changePopulation = true;
		Env.windowSize = 3;

		Env.variableCapability = true;

		Env.printStatistics = true;
		Env.animationGIFFileName = "c:/tmp/anime.gif";

		Env.initClassName = "CenterCircleInitializer";

		Loop.mainLoop();
	}

	public static void main(String[] args) {
		String propertyFile = "props/base.properties";
		if(args.length == 1) {
			propertyFile = args[0];
		}
		dummy();

		// test1();
		// test2();
		// test3();
		// test4();
		// test5();
		// hhtggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffgtest6();
		// test7();
		// test10();
		// test11();
		// test12();

		// test16();
		// test17();

		PropertyManager manager = new PropertyManager();
		Properties props = manager.read(propertyFile);
		manager.interprete(props);

		Loop.mainLoop();
	}
}
