package by.bsuir.portmultithreading.ship;

import java.util.Random;

import by.bsuir.portmultithreading.port.Port;

/**
 * Manages a collection of ships and directs them to a port for processing.
 * <p>
 * The {@code ShipsManager} is responsible for creating a fleet of ships, assigning them to a specified port,
 * and continuously sending them to the port at random intervals for loading or unloading tasks.
 * </p>
 */
public class ShipsManager {
    private Port port;
    private final Ship[] SHIPS;
    private final int NUMBER_OF_SHIPS;

    private final static int MAX_SEND_INTERVAL = 900;
    private final static int MIN_SEND_INTERVAL = 200;
    private final static Random RANDOM_GENERATOR = new Random();

    /**
     * Constructs a new {@code ShipsManager} with the specified number of ships.
     * Each ship is initialized and added to the manager's fleet.
     *
     * @param numberOfShips the number of ships to be managed by this {@code ShipsManager}
     */    
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

    /**
     * Starts the process of sending ships to the port.
     * <p>
     * For each ship in the fleet, if the ship is not already working, a random delay is applied
     * before assigning a new task and sending it to the port. Ships are sent at intervals between
     * {@code MIN_SEND_INTERVAL} and {@code MAX_SEND_INTERVAL} milliseconds.
     * </p>
     */    
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
