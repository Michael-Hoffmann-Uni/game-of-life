package jpp.gol.rules;

import jpp.gol.model.CellState;

public interface Rules {
    /**
     * Returns the state of a field in the next iteration based on the living neighbours and
     * the current state.
     *
     * @param numberOfNeighbors
     * the number of living neighbors.
     * @param currentValue
     * the current state.
     *
     * @return
     * the new state according to the rules.
     **/
    public CellState nextState(int numberOfNeighbors, CellState currentValue);
}
