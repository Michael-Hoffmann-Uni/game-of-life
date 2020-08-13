package jpp.gol.rules;

import jpp.gol.model.CellState;

public class StandardRules implements Rules {
    @Override
    public CellState nextState(int numberOfNeighbors, CellState currentValue) {
        if (numberOfNeighbors < 0 || numberOfNeighbors > 8)
            throw new IllegalNumberOfNeighborsException("Number of neighbors cannot be true.");
        if (currentValue == null)
            throw new NullPointerException("CurrentValue is null.");
        if (currentValue == CellState.DEAD && numberOfNeighbors == 3) {
            return CellState.ALIVE;
        } else if (currentValue == CellState.ALIVE && numberOfNeighbors < 2) {
            return CellState.DEAD;
        } else if (currentValue == CellState.ALIVE && (numberOfNeighbors == 2 || numberOfNeighbors == 3)) {
            return CellState.ALIVE;
        } else if (currentValue == CellState.ALIVE && numberOfNeighbors > 3) {
            return CellState.DEAD;
        } else if (currentValue == CellState.DEAD) {
            return currentValue;
        }
        return currentValue;
    }
}
