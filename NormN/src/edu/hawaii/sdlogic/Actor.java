package edu.hawaii.sdlogic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import edu.hawaii.sdlogic.operant.OperantResource;

/**
 * Actor class
 * @author fujita
 *
 */
public class Actor {
	/**
	 * operant resources
	 */
	private Map<String, OperantResource> resources = new HashMap<String, OperantResource>();

	/**
	 * x coordinate of the location
	 */
	private int x;

	/**
	 * y coordinate of the location
	 */
	private int y;

	/**
	 * output in a period
	 */
	private double performance;

	/**
	 * good exchangers list
	 */
	private LinkedList<Actor>[] exchangers;

	/**
	 * good exchanging locations
	 */
	private int[][] exchangingLocations;

	/**
	 * exchange rate
	 */
	private double exchangeRate = Env.exchangeRate;

	/**
	 * collaborators list
	 */
	private Actor[] friends;

	/**
	 * performance of the collaborators
	 */
	private double[] friendValues;

	/**
	 * reverse link to friends
	 */
	private Set<Actor> reverseFriends;

	/**
	 * age of the actor
	 */
	private int age;

	/**
	 * lifeSpan of the actor
	 */
	private double lifeSpan;

	/**
	 * actor object pool
	 */
	private static Queue<Actor> pool = new LinkedList<Actor>();

	/**
	 * reclaim method for actor instance
	 * @param actor
	 */
	public static void reclaim(Actor actor) {
		actor.clearFriends();
		if(Env.exchangeClassName.equals("MemoryExchange")) {
			for(int i = 0; i < Env.roles + Env.storeRoles; i++) {
				actor.exchangers[i].clear();
			}
		}
		if(Env.exchangeClassName.equals("LocalizedExchange")) {
			for(int i = 0; i < Env.roles + Env.storeRoles; i++) {
				actor.exchangingLocations[i][0] = -1;
				actor.exchangingLocations[i][1] = -1;
			}
		}
		pool.offer(actor);

		actor.age = 0;
		actor.lifeSpan = 0;
		actor.performance = 0;
		actor.x = -1;
		actor.y = -1;
	}

	/**
	 * getInstance method
	 * @return actor object
	 */
	public static Actor getInstance() {
		if(pool.isEmpty()) {
			return new Actor();
		} else {
			return pool.poll();
		}
	}

	/**
	 * main constructor of Actor
	 */
	@SuppressWarnings("unchecked")
	private Actor() {
		friends = new Actor[Env.friends];
		friendValues = new double[Env.friends];
		reverseFriends = new HashSet<Actor>();

		if(Env.exchangeClassName.equals("MemoryExchange")) {
			exchangers = new LinkedList[Env.roles + Env.storeRoles];
			for(int i = 0; i < Env.roles + Env.storeRoles; i++) {
				exchangers[i] = new LinkedList<Actor>();
			}
		}
		if(Env.exchangeClassName.equals("LocalizedExchange")) {
			exchangingLocations = new int[Env.roles + Env.storeRoles][2];
			for(int i = 0; i < Env.roles + Env.storeRoles; i++) {
				exchangingLocations[i][0] = -1;
				exchangingLocations[i][1] = -1;
			}
		}

		for(int i = 0; i < Env.roleNames.length; i++) {
			String ortName = Env.roleNames[i];
			OperantResource ort = new OperantResource(ortName);
			ort.setType(i);

			resources.put(ortName, ort);
		}
	}

	/**
	 * initializer of the fileds
	 */
	public void init() {
		double total = 0;

		for(OperantResource ort: resources.values()) {
			/*
			double effort = Env.rand.nextDouble();
			total += effort;

			ort.setEffort(effort);
			*/
			ort.setEffort(0);

			if(Env.variableCapability) {
				// double capability = 0.9 + 0.1 * Env.rand.nextDouble();
				double capability = Env.rand.nextDouble();
				ort.setSkill(capability);
			} else {
				ort.setSkill(1.0);
			}

			ort.setOutput(0);
			ort.setOutput0(0);
		}

		/*
		for(OperantResource ort: resources.values()) {
			ort.setEffort(ort.getEffort() / total);
		}
		*/

		int roles;

		if(Env.enableStoring2) {
			for(int i = 0; i < Env.roles; i++) {
				OperantResource ort = getOperantResource(Env.roleNames[i]);

				double effort = Env.rand.nextDouble();
				total += effort;
				ort.setEffort(effort);
			}

			for(int i = Env.roles; i < Env.roles + Env.storeRoles; i++) {
				OperantResource ort = getOperantResource(Env.roleNames[i]);

				double effort = Env.rand.nextDouble() / Env.storeRate;
				total += effort;
				ort.setEffort(effort);
			}

			for(int i = Env.roles + Env.storeRoles; i < Env.roleNames.length; i++) {
				OperantResource ort = getOperantResource(Env.roleNames[i]);

				double effort = Env.rand.nextDouble();
				total += effort;
				ort.setEffort(effort);
			}
		} else {
			for(int i = 0; i < Env.roleNames.length; i++) {
				OperantResource ort = getOperantResource(Env.roleNames[i]);

				double effort = Env.rand.nextDouble();
				total += effort;
				ort.setEffort(effort);
			}
		}

		// age = Env.rand.nextInt(Env.lifeSpan);
		age = Env.rand.nextInt(Env.lifeSpan / 10);
		lifeSpan = (int)(Env.lifeSpan + Env.rand.nextGaussian() * 10);
		exchangeRate = Env.exchangeRate;
	}

	/**
	 * 1-dimensional distance of tolus plane
	 *
	 * @param x0
	 * @param x1
	 * @return distance
	 */
	private double diff(double x0, double x1) {
		double diffx0 = x0 - x1;
		double diffx1 = x1 + Env.mapWidth - x0;
		return diffx0 > diffx1? diffx1: diffx0;
	}

	/**
	 * distance for tolus plane between actors
	 *
	 * @param actor
	 * @return distance
	 */
	public double distance(Actor actor) {
		double diffx;
		double diffy;

		if(x > actor.x) {
			diffx = diff(x, actor.x);
		} else {
			diffx = diff(actor.x, x);
		}

		if(y > actor.y) {
			diffy = diff(y, actor.y);
		} else {
			diffy = diff(actor.y, y);
		}

		double distance = Math.sqrt(diffx * diffx + diffy * diffy);

		return distance;
	}

	/**
	 * update method for learning option
	 * @param inc increment resources
	 * @param dec decrement resources
	 */
	public void update(List<String>inc, List<String>dec) {
		for(String otrString: inc) {
			OperantResource otr = resources.get(otrString);
			otr.setEffort(otr.getEffort() * 1.1);;
		}

		for(String otrString: inc) {
			OperantResource otr = resources.get(otrString);
			otr.setEffort(otr.getEffort() * 0.9);;
		}

		double total = 0;
		for(OperantResource otr: resources.values()) {
			total += otr.getEffort();
		}

		for(OperantResource otr: resources.values()) {
			otr.setEffort(otr.getEffort() / total);
		}
	}

	/**
	 * imitate the actor
	 * @param actor
	 */
	public void imitate(Actor actor, boolean assigned) {
		imitate(actor, -1, -1, assigned);
	}

	/**
	 * imitate the actor
	 * @param actor
	 * @param cx -1 or the center of the map
	 * @param cy -1 or the center of the map
	 */
	public void imitate(Actor actor, int cx, int cy) {
		imitate(actor, cx, cy, false);
	}

	public void imitate(Actor actor, int cx, int cy, boolean assigned) {
		// clear friends list and values
		for(int i = 0; i < Env.friends; i++) {
			friends[i] = null;
			friendValues[i] = 0;
		}

		boolean success = false;

		if(!assigned) {
			for(int i = 0; i < 10 && !success; i++) {
				int ix;
				int iy;

				int x0 = actor.getX();
				int y0 = actor.getY();

				// if cx and cy are specified, the location of imitation actor is made to tend to move th (cx, cy).
				if(cx == -1) {
					ix = Env.rand.nextInt(Env.windowSize * 2 + 1) - Env.windowSize;
				} else {
					if(cx > x0) {
						ix = Env.rand.nextInt(Env.windowSize * 2 + 2) - Env.windowSize;
					} else if(cx < x0) {
						ix = Env.rand.nextInt(Env.windowSize * 2 + 2) - Env.windowSize - 1;
					} else {
						ix = Env.rand.nextInt(Env.windowSize * 2 + 1) - Env.windowSize;
					}
				}

				if(cy == -1) {
					iy = Env.rand.nextInt(Env.windowSize * 2 + 1) - Env.windowSize;
				} else {
					if(cy > y0) {
						iy = Env.rand.nextInt(Env.windowSize * 2 + 2) - Env.windowSize;
					} else if(cy < y0) {
						iy = Env.rand.nextInt(Env.windowSize * 2 + 2) - Env.windowSize - 1;
					} else {
						iy = Env.rand.nextInt(Env.windowSize * 2 + 1) - Env.windowSize;
					}
				}

				// new location
				int xx = (x0 + ix + Env.mapWidth) % Env.mapWidth;
				int yy = (y0 + iy + Env.mapHeight) % Env.mapHeight;

				Actor isEmpty = Env.map[xx][yy];

				if(isEmpty == null) {
					// empty location is found.
					Env.map[x][y] = null;
					Env.map[xx][yy] = this;
					x = xx;
					y = yy;
					success = true;
				}
			}
		}

		double total = 0;

		while(total == 0) {
			// imitate values of all operant resources
			for(int i = 0; i < Env.roleNames.length; i++) {
				String otrName = Env.roleNames[i];
				OperantResource mine = resources.get(otrName);
				OperantResource yours = actor.getOperantResource(otrName);

				double value = 0;
				if(!Env.enableStoring2 || i < Env.roles || i >= Env.roles + Env.storeRoles) {
					value = yours.getEffort() + Env.sigmaEffort * Env.rand.nextGaussian();
				} else {
					value = yours.getEffort() + Env.sigmaEffort * Env.rand.nextGaussian() / Env.storeRate;
				}
				if(value < 0) value = 0;

				if(Env.variableCapability) {
					double capability = yours.getSkill() + Env.sigmaSkill * Env.rand.nextGaussian();
					if(capability > 1.0) {
						capability = 1.0;
					} else if(capability < 0.0) {
						capability = 0.0;
					}
					mine.setSkill(capability);
				} else {
					mine.setSkill(1.0);
				}

				mine.setEffort(value);
				total += value;
				mine.setOutput(0);
				mine.setOutput0(0);
			}
		}

		// canonicalize effort values
		for(String otrName: resources.keySet()) {
			OperantResource mine = resources.get(otrName);
			mine.setEffort(mine.getEffort() / total);
		}

		if(Env.exchangeClassName.equals("LocalizedExchange")) {
			for(int i = 0; i < Env.roles + Env.storeRoles; i++) {
				int x = actor.exchangingLocations[i][0];
				int y = actor.exchangingLocations[i][1];

				if(x >= 0 && y >= 0) {
					x = x + (int)(Env.windowSize * Env.rand.nextGaussian());
					y = y + (int)(Env.windowSize * Env.rand.nextGaussian());

					x = (x + Env.mapWidth) % Env.mapWidth;
					y = (y + Env.mapHeight) % Env.mapHeight;

					exchangingLocations[i][0] = x;
					exchangingLocations[i][1] = y;
				} else {
					exchangingLocations[i][0] = -1;
					exchangingLocations[i][1] = -1;
				}
			}
		}
	}

	/**
	 * nextTo method finds an actor close to the focal actor
	 * @param width
	 * @return
	 */
	public Actor nextTo(int width) {
		int ix;
		int iy;

		while(true) {
			ix = Env.rand.nextInt(width * 2 + 1) - width;
			iy = Env.rand.nextInt(width * 2 + 1) - width;

			if(ix != 0 || iy != 0) break;
		}

		int xx = (x + ix + Env.mapWidth) % Env.mapWidth;
		int yy = (y + iy + Env.mapHeight) % Env.mapHeight;

		return Env.map[xx][yy];
	}

	/**
	 * regenerate method
	 * An actor finds and imitates the neighbor actor whose performance is better than it.
	 */
	public void regenerate() {
		boolean success = false;

		for(int i = 0; i < 5 && !success; i++) {
			Actor actor = nextTo(Env.windowSize);

			if(actor != null && actor.getPerformance() > performance) {
				imitate(actor, false);
				success = true;
			}
		}

		if(!success) {
			imitate(this, false);
		}
	}

	public int getActorType() {
		int index = -1;
		double max = -1;

		for(int i = 0; i < Env.roleNames.length; i++) {
			OperantResource ort = getOperantResource(Env.roleNames[i]);
			double effort = ort.getEffort();
			if(effort > max) {
				max = effort;
				index = i;
			}
		}

		return index;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public OperantResource getOperantResource(String name) {
		return resources.get(name);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getPerformance() {
		return performance;
	}

	public void setPerformance(double performance) {
		this.performance = performance;
	}

	public Actor getFriend(int i) {
		return friends[i];
	}

	public void setFriend(int i, Actor friend) {
		if(friends[i] != null) {
			if(friends[i] == friend) {
				return;
			} else {
				friends[i].reverseFriends.remove(this);
			}
		}

		this.friends[i] = friend;
		this.friendValues[i] = 0;
		if(friend != null) {
			friend.reverseFriends.add(this);
		}
	}

	public void clearFriends() {
		for(int i = 0; i < friends.length; i++) {
			if(friends[i] != null) {
				friends[i].reverseFriends.remove(this);
				friends[i] = null;
			}
			friendValues[i] = 0;
		}

		for(Actor actor: reverseFriends) {
			for(int i = 0; i < actor.friends.length; i++) {
				if(actor.friends[i] == this) {
					actor.friends[i] = null;
					actor.friendValues[i] = 0;
				}
			}
		}

		reverseFriends.clear();
	}

	public double getFriendValue(int i) {
		return friendValues[i];
	}

	public void setFriendValue(int i, double friendValue) {
		this.friendValues[i] = friendValue;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int incrementAge() {
		return ++age;
	}

	public double getLifeSpan() {
		return lifeSpan;
	}

	public void setLifeSpan(double lifeSpan) {
		this.lifeSpan = lifeSpan;
	}

	public void addLifeSpan(double lifeSpan) {
		this.lifeSpan += lifeSpan;
	}

	public Set<Actor> getReverseFriends() {
		return reverseFriends;
	}

	public void setReverseFriends(Set<Actor> reverseFriends) {
		this.reverseFriends = reverseFriends;
	}

	public LinkedList<Actor>[] getExchangers() {
		return exchangers;
	}

	public int[][] getExchangingLocations() {
		return exchangingLocations;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
}
