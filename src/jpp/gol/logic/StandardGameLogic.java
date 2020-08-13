package jpp.gol.logic;

import jpp.gol.model.World;
import jpp.gol.rules.Rules;

public class StandardGameLogic implements GameLogic {

    private World world;
    private Rules rules;

    public StandardGameLogic(World world, Rules rules) {
        if (world == null || rules == null) {
            throw new NullPointerException("World or rules is null.");
        }
        this.world = world;
        this.rules = rules;
    }

    @Override
    public void step() {
        World copyWorld = world.clone();
        int width = copyWorld.getWidth();
        int height = copyWorld.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                world.set(j, i, rules.nextState(copyWorld.countNeighbors(j, i), copyWorld.get(j, i)));
            }
        }
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public void changeState(int x, int y) {
        this.world.set(x, y, this.world.get(x, y).invert());
    }
}
