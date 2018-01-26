package edu.hawaii.sdlogic.operant;

/**
 * OperantResource class
 * @author fujita
 *
 */
public class OperantResource {
	/**
	 * resource name
	 */
	private String name;

	/**
	 * effort value
	 */
	private double effort;

	/**
	 * skill level
	 */
	private double skill = 1.0;

	/**
	 * output in a period
	 */
	private double output;

	/**
	 * output before exchange
	 */
	private double output0;

	/**
	 * share for collaboration
	 */
	private double share;

	/**
	 * operant resource type
	 */
	public int type;

	public OperantResource(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getEffort() {
		return effort;
	}

	public void setEffort(double effort) {
		this.effort = effort;
	}

	public double getSkill() {
		return skill;
	}

	public void setSkill(double skill) {
		this.skill = skill;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public void addOutput(double output) {
		this.output += output;
	}

	public double getOutput0() {
		return output0;
	}

	public void setOutput0(double output) {
		this.output0 = output;
	}

	public void addOutput0(double output) {
		this.output0 += output;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getShare() {
		return share;
	}

	public void setShare(double share) {
		this.share = share;
	}

	public void addShare(double share) {
		this.share += share;
	}
}
