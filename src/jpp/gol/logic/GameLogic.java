package jpp.gol.logic;

import jpp.gol.model.World;

public interface GameLogic {
    /**
     * Calculates the next iteration.
     */
    public void step();
    /**
     * Sets a new world.
     *
     * @param world a world object.
     */
    public void setWorld(World world);
    /**
     * Gives the current world.
     *
     * @return the current world.
     */
    public World getWorld();
    /**
     * Changes the state of the field at (x,y) from DEAD to ALIVE and vice versa.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public void changeState(int x, int y);
}
