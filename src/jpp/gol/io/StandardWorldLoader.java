package jpp.gol.io;

import jpp.gol.model.CellState;
import jpp.gol.model.World;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StandardWorldLoader implements WorldLoader {
    @Override
    public World load(InputStream in) throws IOException {
        if (in == null)
            throw new IOException("InputStream is null.");
        //InputStream -> InputStreamReader -> BufferedReader: reading char by char into a StringBuilder
        StringBuilder textBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName(StandardCharsets.UTF_8.name()));
        Reader reader = new BufferedReader(inputStreamReader);
        int c = 0;
        while ((c = reader.read()) != -1) {
            textBuilder.append((char) c);
        }

        //StringBuilder to String
        String worldString = textBuilder.toString();
        //-----------------complete new try ----------------------------------------
        //rn to n
        worldString = worldString.replaceAll("\\r\\n", "\\\n");
        worldString = worldString.replaceAll("\\r", "\\\n");
        //cut world into array
        String[] worldArr = worldString.split("\\n");
        //check special cases
        //Special case of worldString = ""
        if (worldString.equals(""))
            return new World(0, 0);
        if (worldArr.length == 0)
            throw new IOException("WorldArr is empty.");
        //header
        String[] header;
        if (worldArr.length >= 1) {
            header = worldArr[0].split("x");
            if (header.length == 1)
                throw new IOException("Header is invalid - not enough data.");
            if (header.length == 2) {
                int width = Integer.parseInt(header[0]);
                int height = Integer.parseInt(header[1]);
                //WorldString has only header, check if valid
                if (worldArr.length == 1) {
                    if ((width == 0 || height == 0)) {
                        return new World(width, height);
                    } else {
                        throw new IOException("World has only header but neither width nor height is 0.");
                    }
                }
                //check if height is matching length of worldArr
                if (height != worldArr.length - 1)
                    throw new IOException("Width in header does not match content.");
                //check if all content lines are equally long as width
                for (int i = 1; i < worldArr.length; i++) {
                    if (width != worldArr[i].length())
                        throw new IOException("Height in header does not match content.");
                }
                World outWorld = new World(width, height);
                //filling output world
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        if (worldArr[i + 1].charAt(j) == '1') {
                            outWorld.set(j, i, CellState.ALIVE);
                        } else if (worldArr[i + 1].charAt(j) == '0') {
                            outWorld.set(j, i, CellState.DEAD);
                        } else {
                            throw new IOException("Cells must be either 0 or 1.");
                        }
                    }
                }
                return outWorld;
            } else {
                throw new IOException("Header is invalid - too much data.");
            }
        } else {
            throw new IOException("World array has invalid length.");
        }
    }

    @Override
    public void save(World world, OutputStream out) throws IOException {
        if (world == null || out == null)
            throw new IOException("World or OutputStream is null.");
        World copyWorld = world.clone();
        try {
            String header = copyWorld.getWidth() + "x" + copyWorld.getHeight();
            String testString = copyWorld.toString();
            byte[] bytes = (header + "\n" + copyWorld.toString()).getBytes();
            out.write(bytes);
        } catch (Exception e) {
            throw new IOException("Format of input-world is invalid. " + e.toString());
        } finally {
            out.close();
        }
    }
}
