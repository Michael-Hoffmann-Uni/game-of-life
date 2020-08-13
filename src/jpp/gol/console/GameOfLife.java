package jpp.gol.console;

import jpp.gol.io.StandardWorldLoader;
import jpp.gol.logic.ObservableGameLogicDecorator;
import jpp.gol.logic.StandardGameLogic;
import jpp.gol.model.World;
import jpp.gol.rules.StandardRules;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameOfLife {
    public void run(InputStream in, OutputStream out) {
        try {
            ObservableGameLogicDecorator oglDecorator = new ObservableGameLogicDecorator(new StandardGameLogic(new World(), new StandardRules()));
            String input = "";
            String goNext = "2";
            String[] retString;
            int width = 0;
            int height = 0;
            Scanner scanner = new Scanner(in);
            //1.
            out.write("Welcome to Game of Life.\n".getBytes());
            outerLoop:
            while (true) {
                switch (goNext) {
                    case "2":
                        goNext = two(out, scanner);
                        continue;
                    case "3":
                        retString = three(out, scanner);
                        if (retString[1] != "x") {
                            File file = new File(retString[1]);
                            FileInputStream fileStream = new FileInputStream(file);
                            StandardWorldLoader worldLoader = new StandardWorldLoader();
                            oglDecorator.setWorld(worldLoader.load(fileStream));
                        }
                        goNext = retString[0];
                        continue;
                    case "4":
                        retString = four(out, scanner);
                        if (retString[1] != "x")
                            height = Integer.parseInt(retString[1]);
                        goNext = retString[0];
                        continue;
                    case "5":
                        retString = five(out, scanner);
                        if (retString[1] != "x") {
                            width = Integer.parseInt(retString[1]);
                            oglDecorator.setWorld(new World(width, height));
                        }
                        goNext = retString[0];
                        continue;
                    case "6":
                        out.write(("\n" + oglDecorator.getWorld().toString() + "\n\n").getBytes());
                        goNext = "7";
                        continue;
                    case "7":
                        goNext = seven(out, scanner);
                        continue;
                    case "8":
                        retString = eight(out, scanner, width, height);
                        if (retString[1] != "x") {
                            String[] splitter = retString[1].split(",", 2);
                            int recX = Integer.parseInt(splitter[0]);
                            int recY = Integer.parseInt(splitter[1]);
                            World world = oglDecorator.getWorld();
                            world.set(recX, recY, world.get(recX, recY).invert());
                        }
                        goNext = retString[0];
                        continue;
                    case "9":
                        out.write("\n".getBytes());
                        goNext = "10";
                        continue;
                    case "10":
                        out.write("Game ist starting.\n".getBytes());
                        goNext = "11";
                        continue;
                    case "11":
                        out.write(("\n" + oglDecorator.getWorld().toString() + "\n\n").getBytes());
                        goNext = "12";
                        continue;
                    case "12":
                        retString = twelve(out, scanner);
                        if (retString[1].equals("step")) {
                            oglDecorator.step();
                        }
                        goNext = retString[0];
                        continue;
                    case "13":
                        out.write("Goodbye!\n".getBytes());
                        goNext = "14";
                        continue;
                    case "14":
                        break outerLoop;
                }
            }
            scanner.close();
            out.close();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        GameOfLife gol = new GameOfLife();
        gol.run(System.in, System.out);
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String two(OutputStream out, Scanner scanner) {
        try {
            String input = "";
            out.write("Would you like to load a world from a [f]ile or [c]onfigure one yourself?\n".getBytes());
            input = scanner.nextLine();
            if (input.equals("f"))
                return "3";
            if (input.equals("c"))
                return "4";
            out.write(("Invalid action! The only valid actions are f and c.\n").getBytes());
            return "2";
        } catch (Exception e) {
            return "2";
        }
    }

    public String[] three(OutputStream out, Scanner scanner) {
        try {
            String input = "";
            out.write("Enter the path to a file:\n".getBytes());
            input = scanner.nextLine();
            if (input.matches("^(/[a-zA-Z0-9\\-]+)+\\.txt$") == false) {
                out.write("Invalid path or the content of the file is incorrect.\n".getBytes());
                return new String[]{"2", "x"};
            } else {
                File file = new File(input);
                if (!file.exists()) {
                    out.write("Invalid path or the content of the file is incorrect.\n".getBytes());
                    return new String[]{"2", "x"};
                } else {
                    return new String[]{"9", input};
                }
            }
        } catch (Exception e) {
            return new String[]{"2", "x"};
        }
    }

    public String[] four(OutputStream out, Scanner scanner) {
        try {
            String input = "";
            out.write("Enter the height of the world:\n".getBytes());
            input = scanner.nextLine();
            if (isNumeric(input)) {
                if (Integer.parseInt(input) < 1) {
                    out.write("Invalid height.\n".getBytes());
                    return new String[]{"4", "x"};
                } else {
                    return new String[]{"5", input};
                }
            } else {
                out.write("Invalid height.\n".getBytes());
                return new String[]{"4", "x"};
            }
        } catch (Exception e) {
            return new String[]{"4", "0"};
        }
    }

    public String[] five(OutputStream out, Scanner scanner) {
        try {
            String input = "";
            out.write("Enter the width of the world:\n".getBytes());
            input = scanner.nextLine();
            if (isNumeric(input)) {
                if (Integer.parseInt(input) < 1) {
                    out.write("Invalid width.\n".getBytes());
                    return new String[]{"5", "x"};
                } else {
                    return new String[]{"6", input};
                }
            } else {
                out.write("Invalid width.\n".getBytes());
                return new String[]{"5", "x"};
            }
        } catch (Exception e) {
            return new String[]{"5", "0"};
        }
    }

    public String seven(OutputStream out, Scanner scanner) {
        try {
            String input = "";
            out.write("Would you like to change a field? (yes/no)\n".getBytes());
            input = scanner.nextLine();
            if (input.equals("no")) {
                return "9";
            } else if (input.equals("yes")) {
                return "8";
            } else {
                out.write("Invalid input.\n".getBytes());
                return "7";
            }
        } catch (Exception e) {
            return "7";
        }
    }

    public String[] eight(OutputStream out, Scanner scanner, int width, int height) {
        try {
            String input = "";
            out.write("Enter the x- and y-coordinates of the field you would like to change (format: <x>,<y>):\n".getBytes());
            input = scanner.nextLine();
            String patternString = "[0-9]*[,][0-9]*";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(input);
            int inputX = 0;
            int inputY = 0;
            if (matcher.matches()) {
                String[] coordArray = input.split(",", 2);
                inputX = Integer.parseInt(coordArray[0]);
                inputY = Integer.parseInt(coordArray[1]);
                if (inputX > width - 1 || inputX < 0 || inputY > height - 1 || inputY < 0) {
                    out.write("Invalid coordinates!\n".getBytes());
                    return new String[]{"6", "x"};
                } else {
                    return new String[]{"6", inputX + "," + inputY};
                }
            } else {
                out.write("Invalid coordinates!\n".getBytes());
                return new String[]{"6", "x"};
            }
        } catch (Exception e) {
            return new String[]{"6", "x"};
        }
    }

    public String[] twelve(OutputStream out, Scanner scanner) {
        try {
            String input = "";
            out.write("Would you like to calculate the next [i]teration, or [e]nd the game?\n".getBytes());
            input = scanner.nextLine();
            if (input.equals("i")) {
                return new String[]{"11", "step"};
            } else if (input.equals("e")) {
                return new String[]{"13", "x"};
            } else {
                out.write("Invalid action! The only valid actions are i and e.\n".getBytes());
                return new String[]{"12", "x"};
            }
        } catch (Exception e) {
            return new String[]{"12", "step"};
        }
    }
}
