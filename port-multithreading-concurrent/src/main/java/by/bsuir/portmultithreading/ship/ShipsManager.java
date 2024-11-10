package by.bsuir.portmultithreading.ship;

import by.bsuir.portmultithreading.port.Port;

public class ShipsManager {
    private Port port;
    private final Ship[] SHIPS;
    private final int NUMBER_OF_SHIPS;

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
                    try {
                        Thread.sleep(525);  // Simulate some delay when sending ships
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
