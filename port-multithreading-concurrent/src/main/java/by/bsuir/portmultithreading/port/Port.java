package by.bsuir.portmultithreading.port;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.concurrent.PriorityBlockingQueue;

import by.bsuir.portmultithreading.ship.Ship;

/**
 * Represents a port that manages berths for ships to perform loading and unloading operations.
 * <p>
 * The {@code Port} class handles a queue of ships waiting for berthing, assigns ships to available berths,
 * and logs information about port operations. A background thread processes ships and manages berthing tasks.
 * </p>
 */
public class Port {
    private final Warehouse WAREHOUSE = new Warehouse();
    private final int NUMBER_OF_BERTHS;
    private final Berth[] BERTHS;

    private PriorityBlockingQueue<Ship> shipsQueue = new PriorityBlockingQueue<>(10, Collections.reverseOrder());

    /**
     * Constructs a new {@code Port} with the specified number of berths.
     * Each berth is initialized with a reference to the port's warehouse.
     *
     * @param numberOfBerths the number of berths available at the port
     */    
    public Port(int numberOfBerths) {
        NUMBER_OF_BERTHS = numberOfBerths;
        BERTHS = new Berth[NUMBER_OF_BERTHS];
        for (int i = 0; i < NUMBER_OF_BERTHS; ++i) {
            BERTHS[i] = new Berth(WAREHOUSE);
        }
    }

    /**
     * Starts processing ships at the port.
     * <p>
     * This method launches a background thread that continuously monitors the ship queue
     * and assigns ships to available berths as they arrive.
     * </p>
     */    
    public void startProcessingShips() {
        new ProcessingThread().start();
    }

    /**
     * Adds a ship to the port's processing queue.
     * <p>
     * This method places the ship in the queue of waiting ships and notifies the processing thread.
     * </p>
     *
     * @param ship the {@code Ship} instance to be added to the queue for processing
     */    
    public void addShip(Ship ship) {
        shipsQueue.offer(ship);
    }

    /**
     * Inner class responsible for processing ships waiting to berth at the port.
     * <p>
     * The {@code ProcessingThread} monitors the ship queue, assigns available berths to incoming ships, 
     * and logs the port's operations to a file at regular intervals.
     * </p>
     */    
    private class ProcessingThread extends Thread {
        private final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
        private final static int LOGGING_PERIOD_SEC = 5;  
        private LocalTime timeOfLastLog = LocalTime.now();

        private static final File LOG_FILE;

        static {
            LOG_FILE = new File("port.log");
            try {
                LOG_FILE.createNewFile();
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to create file: " + LOG_FILE.getName(), e);
            }
        }

        /**
         * Runs the processing thread.
         * <p>
         * The thread continuously checks for waiting ships, assigns berths if available, and logs port activity.
         * If a ship requires loading and the warehouse lacks sufficient goods, it triggers an order for more goods.
         * </p>
         */        
        @Override
        public void run() {
            while (true) {
                try {
                    Ship ship = shipsQueue.take();
                    if (ship.getOperation() == Ship.Operation.LOADING 
                            && !WAREHOUSE.isEnoughGoods(ship.getNeededGoods())) {
                        WAREHOUSE.orderMoreGoodsFromSuppliers();
                    }

                    assignBerth(ship);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                printLog();
            }
        }

        /**
         * Assigns a ship to the first available berth.
         * <p>
         * If all berths are occupied, the method waits briefly and rechecks availability.
         * </p>
         *
         * @param ship the {@code Ship} to be assigned to a berth
         */        
        private void assignBerth(Ship ship) {
            while (true) {
                for (Berth berth : BERTHS) {
                    if (!berth.isWorking()) {
                        berth.startBerthing(ship);
                        return;
                    }
                }

                try {
                    Thread.sleep(100);  // Wait if all berths are busy
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        /**
         * Logs port activities to a file at a regular interval.
         * <p>
         * The log includes the status of waiting ships, the berths, and the number of goods in the warehouse.
         * </p>
         */        
        private void printLog() {
            LocalTime currentTime = LocalTime.now();
            if (currentTime.toSecondOfDay() >= timeOfLastLog.toSecondOfDay() + LOGGING_PERIOD_SEC) {
                String log = createLogString();
                try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                    fileWriter.append(log);
                }
                catch (IOException e) {
                    throw new RuntimeException("Failed to write log to file: " + LOG_FILE.getName(), e);
                }

                timeOfLastLog = currentTime;
            }
        }

        /**
         * Builds a log string with information about the current state of the port.
         * <p>
         * The log includes the total number of goods in the warehouse, waiting ships, and the status of berths.
         * </p>
         *
         * @return a {@code String} representing the log message
         */        
        private String createLogString() {
            StringBuilder logStringBuilder = new StringBuilder();

            logStringBuilder.append(LocalTime.now().format(TIME_FORMAT) + ":\n");
            logStringBuilder.append("Total number of goods in warehouse is " + WAREHOUSE.getNuberOfGoods() + "\n");
            addWaitingShipsToLog(logStringBuilder);
            addShipsOnBerthsToLog(logStringBuilder);
            logStringBuilder.append("-".repeat(142) + "\n");
        
            return logStringBuilder.toString();
        }

        private void addWaitingShipsToLog(StringBuilder logStringBuilder) {
            logStringBuilder.append("Ships waiting for berthing:\n");
            for (Ship ship : shipsQueue) {
                logStringBuilder.append("Ship with id " + ship.getId() + ", " 
                        + "ship priority " + ship.getPriority() + ", "
                        + "cargo priority " + ship.getCargoPriority() + ", "
                        + "is waiting for " + ship.getOperation() + ", "
                        + "number of cargo " + ship.getNumberOfCargo() + ", "
                        + "specified duration of berthing " + ship.getSpecifiedBerthingTime() + " ms\n");
            }
        }

        private void addShipsOnBerthsToLog(StringBuilder logStringBuilder) {
            logStringBuilder.append("Berths status: \n");
            for (Berth berth : BERTHS) {
                if (berth.isWorking()) {
                    Ship shipOnBerth = berth.getBerthingShip();
                    logStringBuilder.append("Berth with id " + berth.getId() + " is " + shipOnBerth.getOperation() + " "
                            + "ship with id " + shipOnBerth.getId() + ", "
                            + "ship priority " + shipOnBerth.getPriority() + ", "
                            + "cargo priority " + shipOnBerth.getCargoPriority() + ", "
                            + "number of cargo " + shipOnBerth.getNumberOfCargo() + ", "
                            + "specified duration of berthing " + shipOnBerth.getSpecifiedBerthingTime() + " ms\n"); 
                }
                else {
                    logStringBuilder.append("Berth with id " + berth.getId() + " is free\n"); 
                }
            }
        }
    }
}
