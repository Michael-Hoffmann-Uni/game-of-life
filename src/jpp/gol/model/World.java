package jpp.gol.model;

import java.util.ArrayList;
import java.util.Arrays;

public class World {

    private CellState[][] board;

    public World() {
        board = new CellState[10][10];
        for (CellState[] row : board)
            Arrays.fill(row, CellState.DEAD);
    }

    public World(int width, int height) {
        if (width <= 0 || height <= 0)
            throw new IllegalWorldSizeException("Height and width have to be greater than zero.");
        //FROM NOW ON: FIRST INDEX IS WIDTH - SECOND INDEX IS HEIGHT
        //board.length is width --- board[0].length = height
        board = new CellState[width][height];
        for (CellState[] row : board)
            Arrays.fill(row, CellState.DEAD);
    }

    public World(World other) {
        if (other == null)
            throw new NullPointerException("Other-world is null.");
        this.board = new CellState[other.getWidth()][other.getHeight()];
        for (int i = 0; i < other.getHeight(); i++) {
            for (int j = 0; j < other.getWidth(); j++) {
                this.set(j, i, other.get(j, i));
            }
        }
    }

    public CellState[][] getBoard() {
        return board;
    }

    public int countNeighbors(int x, int y) {
        int retVal = 0;
        //top-left
        if (x > 0 && y > 0) {
            retVal += get(x - 1, y - 1).getScore();
        } else if (x > 0 && y == 0) {
            retVal += get(x - 1, getHeight() - 1).getScore();
        } else if (x == 0 && y > 0) {
            retVal += get(getWidth() - 1, y - 1).getScore();
        } else {
            retVal += get(getWidth() - 1, getHeight() - 1).getScore();
        }
        //top-middle
        if (y > 0) {
            retVal += get(x, y - 1).getScore();
        } else {
            retVal += get(x, getHeight() - 1).getScore();
        }
        //top-right
        if (x < getWidth() - 1 && y > 0) {
            retVal += get(x + 1, y - 1).getScore();
        } else if (x < getWidth() - 1 && y == 0) {
            retVal += get(x + 1, getHeight() - 1).getScore();
        } else if (x == getWidth() - 1 && y > 0) {
            retVal += get(0, y - 1).getScore();
        } else {
            retVal += get(0, getHeight() - 1).getScore();
        }
        // left-middle
        if (x > 0) {
            retVal += get(x - 1, y).getScore();
        } else {
            retVal += get(getWidth() - 1, y).getScore();
        }
        //right-middle
        if (x < getWidth() - 1) {
            retVal += get(x + 1, y).getScore();
        } else {
            retVal += get(0, y).getScore();
        }
        //bottom-left
        if (x > 0 && y < getHeight() - 1) {
            retVal += get(x - 1, y + 1).getScore();
        } else if (x > 0 && y == getHeight() - 1) {
            retVal += get(x - 1, 0).getScore();
        } else if (x == 0 && y < getHeight() - 1) {
            retVal += get(getWidth() - 1, y + 1).getScore();
        } else {
            retVal += get(getWidth() - 1, 0).getScore();
        }
        //bottom-middle
        if (y < getHeight() - 1) {
            retVal += get(x, y + 1).getScore();
        } else {
            retVal += get(x, 0).getScore();
        }
        //bottom-right
        if (x < getWidth() - 1 && y < getHeight() - 1) {
            retVal += get(x + 1, y + 1).getScore();
        } else if (x < getWidth() - 1 && y == getHeight() - 1) {
            retVal += get(x + 1, 0).getScore();
        } else if (x == getWidth() - 1 && y < getHeight() - 1) {
            retVal += get(0, y + 1).getScore();
        } else {
            retVal += get(0, 0).getScore();
        }
        return retVal;
    }


    public int getWidth() {
        return board.length;
    }

    public int getHeight() {
        return board[0].length;
    }

    public void set(int x, int y, CellState value) {
        if (value == null)
            throw new NullPointerException("Value of Cell is null.");
        if (x > this.getWidth() || y > this.getHeight() || x < 0 || y < 0)
            throw new IllegalCoordinateException("One of the coordinates is not inside the world.");
        board[x][y] = value;
    }

    public CellState get(int x, int y) {
        if (x > this.getWidth() || y > this.getHeight() || x < 0 || y < 0)
            throw new IllegalCoordinateException("One of the coordinates is not inside the world.");
        return board[x][y];
    }

    public String toString() {
        String outputString = "";
        //Height - Width - J - I
        //board.length
        for (int i = 0; i < this.getHeight(); i++) {
            if (i != 0)
                outputString = outputString + "\n";
            //board[0].length
            for (int j = 0; j < this.getWidth(); j++) {
                if (this.get(j, i).equals(CellState.ALIVE)) {
                    outputString = outputString + "1";
                } else {
                    outputString = outputString + "0";
                }
            }
        }
        return outputString;
    }

    public World clone() {
        World outputWorld = new World(this.getWidth(), this.getHeight());
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                outputWorld.set(j, i, this.get(j, i));
            }
        }
        return outputWorld;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        World world = (World) o;
        if (this.getHeight() != world.getHeight())
            return false;
        if (this.getWidth() != world.getWidth())
            return false;
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                if (this.get(j, i) != world.get(j, i))
                    return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int reVal = 0;
        for (int i = 0; i < board.length; i++) {
            reVal += Arrays.hashCode(board[i]);
        }
        return reVal;
    }
}
