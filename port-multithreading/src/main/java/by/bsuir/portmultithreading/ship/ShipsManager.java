package by.bsuir.portmultithreading.ship;

import java.util.Random;

import by.bsuir.portmultithreading.port.Port;

public class ShipsManager {
    private Port port;
    private final Ship[] SHIPS;
    private final int NUMBER_OF_SHIPS;

    private final static int MAX_SEND_INTERVAL = 900;
    private final static int MIN_SEND_INTERVAL = 200;
    private final static Random RANDOM_GENERATOR = new Random();

    public ShipsManager(int numberOfShips) {
        NUMBER_OF_SHIPS = numberOfShips;
        SHIPS = new Ship[NUMBER_OF_SHIPS];
        for (int i = 0; i < NUMBER_OF_SHIPS; ++i) {
            SHIPS[i] = new Ship();
        }
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public void startSendingShips() {
        while (true) {
            for (Ship ship : SHIPS) {
                if (!ship.isWorking()) {
                    int preparingShipTime = MIN_SEND_INTERVAL +
                            RANDOM_GENERATOR.nextInt((MAX_SEND_INTERVAL - MIN_SEND_INTERVAL) + 1);
                    try {
                        Thread.sleep(preparingShipTime);
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    ship.createRandomTask();
                    ship.startWorking();
                    port.addShip(ship);
                }
            }
        }
    }
}
