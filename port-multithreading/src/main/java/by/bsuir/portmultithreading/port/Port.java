package by.bsuir.portmultithreading.port;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.PriorityQueue;

import by.bsuir.portmultithreading.ship.Ship;

public class Port {
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

        @Override
        public void run() {
            while (true) {
                synchronized (QUEUE_LOCK) {
                    waitForShips();

                    Ship ship = shipsQueue.poll();
                    if (ship.getOperation() == Ship.Operation.LOADING 
                            && !WAREHOUSE.isEnoughGoods(ship.getNeededGoods())) {
                        WAREHOUSE.orderMoreGoodsFromSuppliers();
                    }

                    assignBerth(ship);
                    printLog();
                }
            }
        }

        private void waitForShips() {
            while (shipsQueue.isEmpty()) {
                try {
                    QUEUE_LOCK.wait();
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

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
            logStringBuilder.append("Ships on berths: \n");
            for (Berth berth : BERTHS) {
                Ship shipOnBerth = berth.getBerthingShip();
                logStringBuilder.append("Berth with id " + berth.getId() + " is " + shipOnBerth.getOperation() + " "
                        + "ship with id " + shipOnBerth.getId() + ", "
                        + "ship priority " + shipOnBerth.getPriority() + ", "
                        + "cargo priority " + shipOnBerth.getCargoPriority() + ", "
                        + "number of cargo " + shipOnBerth.getNumberOfCargo() + ", "
                        + "specified duration of berthing " + shipOnBerth.getSpecifiedBerthingTime() + " ms\n"); 
            }
        }
    }

    private final Warehouse WAREHOUSE = new Warehouse();
    private final int NUMBER_OF_BERTHS;
    private final Berth[] BERTHS;

    private PriorityQueue<Ship> shipsQueue = new PriorityQueue<>(Collections.reverseOrder());
    private static final Object QUEUE_LOCK = new Object();

    public Port(int numberOfBerths) {
        NUMBER_OF_BERTHS = numberOfBerths;
        BERTHS = new Berth[NUMBER_OF_BERTHS];
        for (int i = 0; i < NUMBER_OF_BERTHS; ++i) {
            BERTHS[i] = new Berth(WAREHOUSE);
        }
    }

    public void startProcessingShips() {
        new ProcessingThread().start();
    }

    public void addShip(Ship ship) {
        synchronized (QUEUE_LOCK) {
            shipsQueue.offer(ship);
            QUEUE_LOCK.notify();
        }
    }
}
