package by.bsuir.portmultithreading.port;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import by.bsuir.portmultithreading.ship.Ship;
import by.bsuir.portmultithreading.ship.Ship.Operation;

/**
 * Represents a berth in the port where ships can load or unload goods.
 * <p>
 * Each {@code Berth} has a unique ID, an assigned ship during berthing, and
 * records the specified and actual times for the berthing operation.
 * </p>
 */
public class Berth {
    private static int berthsCounter = 0;
    private int id;

    private boolean isWorking = false;

    private Ship berthingShip;
    private Operation operation;

    private long specifiedTimeOfBerthing = 0L;
    private long actualTimeOfBerthing = 0L;

    private final Warehouse WAREHOUSE;

    /**
     * Constructs a new {@code Berth} associated with a given warehouse.
     *
     * @param warehouse the {@code Warehouse} associated with this berth
     */
    public Berth (Warehouse warehouse) {
        id = ++berthsCounter;
        WAREHOUSE = warehouse;
    }

    /**
     * Starts the berthing operation for a specified ship.
     * <p>
     * This method assigns the ship to the berth and initiates a {@code BerthThread}
     * to handle the loading or unloading operation.
     * </p>
     *
     * @param ship the {@code Ship} to be berthed
     */
    public void startBerthing(Ship ship) {
        isWorking = true;
        berthingShip = ship;
        specifiedTimeOfBerthing = ship.getSpecifiedBerthingTime();
        operation = berthingShip.getOperation();
        new BerthThread().start();
    }

    public boolean isWorking() {
        return isWorking;
    }

    public int getId() {
        return id;
    }

    public Ship getBerthingShip() {
        return berthingShip;
    }

    /**
     * Inner class responsible for executing the loading or unloading operation of a ship.
     * <p>
     * The {@code BerthThread} manages the berthing process, logs start and completion,
     * adjusts the ship's priority based on berthing time, and logs any penalties.
     * </p>
     */
    private class BerthThread extends Thread {
        private final static DateTimeFormatter TERMINAL_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        private final static DateTimeFormatter LOG_FILE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.S");

        private static final File LOG_FILE;
        private static final Object LOG_FILE_LOCK = new Object();

        static {
            LOG_FILE = new File("not_on_time.log");
            try {
                LOG_FILE.createNewFile();
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to create file: " + LOG_FILE.getName(), e);
            }
        }

        /**
         * Runs the berthing operation for the assigned ship.
         * <p>
         * This includes loading or unloading cargo, logging start and completion times,
         * and adjusting ship priority based on berthing performance.
         * </p>
         */
        @Override
        public void run() {
            if (operation == Operation.LOADING) {
                WAREHOUSE.sendGoodsToBerth(berthingShip.getNeededGoods());
            }

            printStartLog();
            loadOrUnloadShip();
            printCompleteLog();

            if (operation == Operation.UNLOADING) {
                WAREHOUSE.recieveGoodsFromBerth(berthingShip.getNumberOfCargo());
            }

            actualTimeOfBerthing = berthingShip.getActualBerthingTime();
            checkIfShipDoneOnTime();

            berthingShip.finishWorking();
            isWorking = false;
        }

        private void printStartLog() {
            System.out.println(LocalTime.now().format(TERMINAL_TIME_FORMAT) + ":  " + "Ship with id " + berthingShip.getId() + ", " 
                    + "ship priority " + berthingShip.getPriority() + ", "
                    + "cargo priority " + berthingShip.getCargoPriority() + ", "
                    + "has started " + operation + " on berth " + id + ", "
                    + "number of cargo " + berthingShip.getNumberOfCargo() + ", "
                    + "specified duration of berthing " + specifiedTimeOfBerthing + " ms");
        }

        private void printCompleteLog() {
            System.out.println(LocalTime.now().format(TERMINAL_TIME_FORMAT) + ":  " + "Ship with id " + berthingShip.getId() + ", " 
                    + "ship priority " + berthingShip.getPriority() + ", "
                    + "cargo priority " + berthingShip.getCargoPriority() + ", "
                    + "has completed " + operation + " on berth " + id + ", "
                    + "number of cargo " + berthingShip.getNumberOfCargo() + ", "
                    + "actual duration of berthing " + berthingShip.getActualBerthingTime() + " ms");
        }

        /**
         * Simulates the time taken for the loading or unloading operation.
         */
        private void loadOrUnloadShip() {
            try {
                sleep(berthingShip.getActualBerthingTime());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * Checks if the ship completed berthing within the specified time.
         * <p>
         * If berthing exceeds the specified time, the ship's priority is reduced as a penalty;
         * otherwise, it is increased as a reward.
         * </p>
         */
        private void checkIfShipDoneOnTime() {
            if (isBerthingTimeExceeded()) {
                printPunishLog();
                berthingShip.decreasePriority();    // Punish ship
            }
            else {
                berthingShip.increasePriority();    // Reward ship
            }
        }

        /**
         * Logs a penalty if the berthing operation exceeds the specified time.
         * <p>
         * This log is written to a file, indicating that the ship's priority has been decreased.
         * </p>
         */
        private void printPunishLog() {
            String log = LocalTime.now().format(LOG_FILE_TIME_FORMAT) + ":  " 
                    + "Berthing time of ship with id " + berthingShip.getId() + ", " 
                    + "priority " + berthingShip.getPriority() + ", "
                    + "exceeded specified time, "
                    + "decreasing ship priority for next time\n";
                    
            synchronized (LOG_FILE_LOCK) {
                try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                    fileWriter.append(log);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean isBerthingTimeExceeded() {
            return actualTimeOfBerthing > specifiedTimeOfBerthing;
        }
    }
}
