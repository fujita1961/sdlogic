package edu.hawaii.sdlogic.initializer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import edu.hawaii.sdlogic.Actor;
import edu.hawaii.sdlogic.Env;
import edu.hawaii.sdlogic.operant.OperantResource;

public class FileInitializer extends Initializer {
	/**
	 * file Name
	 */
	private String fileName;

	/**
	 * Constructor
	 * @param fileName
	 */
	public FileInitializer(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * init method
	 */
	@Override
	public void init() {
		Env.rand = new Random();
		Env.map = new Actor[Env.mapWidth][Env.mapHeight];
		Env.actorList = new ArrayList<Actor>();

		initTypes();

		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String line;

			line = br.readLine();
			String[] words = line.split(" ");

			while((line = br.readLine()) != null) {
				Actor actor = Actor.getInstance();
				actor.init();

				words = line.split(" ");

				int count = 0;

				int x = Integer.parseInt(words[count++]);
				int y = Integer.parseInt(words[count++]);

				actor.setX(x);
				actor.setY(y);

				for(int i = 0; i < Env.typeNames.length; i++) {
					String ortName = Env.typeNames[i];
					double capability = Double.parseDouble(words[count++]);
					double effort = Double.parseDouble(words[count++]);

					OperantResource ort = actor.getOperantResource(ortName);
					ort.setSkill(capability);
					ort.setEffort(effort);
				}

				Env.actorList.add(actor);
				Env.map[actor.getX()][actor.getY()] = actor;
			}

			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		initFertilityAndOutputFunction();
	}
}
