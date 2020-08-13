package jpp.gol.logic;

import jpp.gol.model.World;

/**
 * Interface for classes that need to get informed about changes to the world.
 */
public interface WorldChangedListener {
    /**
     * Called with the new world state.
     *
     * @param world the current world.
     */
    void onChange(World world);
}
