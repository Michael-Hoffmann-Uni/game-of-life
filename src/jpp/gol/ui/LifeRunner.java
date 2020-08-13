package jpp.gol.ui;

import javafx.application.Platform;
import jpp.gol.logic.ObservableGameLogicDecorator;
import jpp.gol.model.World;

import java.util.function.Consumer;

public class LifeRunner implements Runnable {
    private int speed;
    private boolean exit;
    private ObservableGameLogicDecorator oglDecorator;
    private Consumer<ObservableGameLogicDecorator> lifeUpdateCallback;
    Thread t;

    public LifeRunner(ObservableGameLogicDecorator oglDecorator, int speed, Consumer<ObservableGameLogicDecorator> lifeUpdateCallback) {
        this.speed = speed;
        this.oglDecorator = oglDecorator;
        this.lifeUpdateCallback = lifeUpdateCallback;
        this.exit = false;
        System.out.println("New lifeRunner started.");
        t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void run() {
        int i = 0;
        while (exit == false) {
            System.out.println("Run: " + i);
            i++;
            try {
                Thread.sleep(Long.valueOf(speed));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            World world = oglDecorator.getWorld();
            oglDecorator.step();
            oglDecorator.setWorld(world);
            Platform.runLater(() -> lifeUpdateCallback.accept(oglDecorator));
        }
        System.out.println("lifeRunner stopped!");
    }

    public void stop() {
        exit = true;
    }
}
