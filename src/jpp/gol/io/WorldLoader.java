package jpp.gol.io;

import jpp.gol.model.World;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface WorldLoader {
    /**
     * Loads a World from a given file format.
     *
     * @param in
     * an input stream where the world can be read from.
     *
     * @return
     * the world as read from the stream.
     *
     * @throws IOException
     * if an error occurs while reading the world from the input stream.
     **/
    public World load(InputStream in) throws IOException;

    /**
     * Writes a given world onto an output stream.
     *
     * @param world
     * the world that will be written on the stream.
     * @param out
     * the stream to write to.
     *
     * @throws IOException
     * if an error occurs while writing the world onto the stream.
     **/
    public void save(World world, OutputStream out) throws IOException;
}
