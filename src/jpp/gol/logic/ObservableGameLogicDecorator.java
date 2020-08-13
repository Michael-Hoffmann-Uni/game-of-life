package jpp.gol.logic;

import jpp.gol.model.World;

import java.util.ArrayList;
import java.util.List;

public class ObservableGameLogicDecorator implements GameLogic {
    private List<WorldChangedListener> wclList;
    private GameLogic delegate;
    private int speed;

    public ObservableGameLogicDecorator(GameLogic delegate) {
        if (delegate == null)
            throw new NullPointerException("Delegate is null.");
        this.delegate = delegate;
        wclList = new ArrayList<>();
    }

    public void addWorldChangedListener(WorldChangedListener listener) {
        if (listener == null)
            throw new NullPointerException("Listener is null.");
        if (wclList.contains(listener))
            throw new IllegalArgumentException("This listener is already registered.");
        wclList.add(listener);
    }

    public void removeWorldChangedListener(WorldChangedListener listener) {
        if (listener == null)
            throw new NullPointerException("Listener is null");
        if (wclList.isEmpty())
            throw new IllegalArgumentException("There are no listeners registered.");
        if (wclList.contains(listener)) {
            wclList.remove(listener);
        } else {
            throw new IllegalArgumentException("This listener is not registered.");
        }
    }

    @Override
    public void step() {
        this.delegate.step();
        for (WorldChangedListener wcl : wclList)
            wcl.onChange(delegate.getWorld());
    }

    @Override
    public void setWorld(World world) {
        this.delegate.setWorld(world);
        for (WorldChangedListener wcl : wclList)
            wcl.onChange(world);
    }

    @Override
    public World getWorld() {
        return delegate.getWorld();
    }

    @Override
    public void changeState(int x, int y) {
        delegate.changeState(x, y);
        for (WorldChangedListener wcl : wclList)
            wcl.onChange(delegate.getWorld());
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
