package by.bsuir.portmultithreading.ship;

import by.bsuir.portmultithreading.port.Port;

public class ShipsManager {
    private Port port;
    private Ship[] ships;
    private final Integer NUMBER_OF_SHIPS;

    public ShipsManager(Integer numberOfShips) {
        NUMBER_OF_SHIPS = numberOfShips;
        ships = new Ship[NUMBER_OF_SHIPS];
        for (int i = 0; i < NUMBER_OF_SHIPS; ++i) {
            ships[i] = new Ship();
        }
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public void startSendingShips() {
        while (true) {
            for (Ship ship : ships) {
                if (!ship.isWorking()) {
                    ship.createRandomTask();
                    ship.startWorking();
                    port.addShipToPort(ship);
                    try {
                        Thread.sleep(500); // Simulate some delay
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
