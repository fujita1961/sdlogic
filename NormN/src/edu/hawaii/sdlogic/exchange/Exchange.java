package edu.hawaii.sdlogic.exchange;

import java.util.Set;

import edu.hawaii.sdlogic.Actor;

/**
 * exchange interface
 * @author fujita
 *
 */
public interface Exchange {
	public void exchange(Set<Actor> satisfiedActors);
}
