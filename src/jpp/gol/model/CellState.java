package jpp.gol.model;

public enum CellState {
    DEAD,
    ALIVE;

    public static CellState fromBoolean(boolean b) {
        if (b)
            return CellState.ALIVE;
        return CellState.DEAD;
    }

    public int getScore() {
        if (this.equals(CellState.ALIVE))
            return 1;
        return 0;
    }

    public CellState invert() {
        if (this.equals(CellState.ALIVE))
            return CellState.DEAD;
        return CellState.ALIVE;
    }
}
