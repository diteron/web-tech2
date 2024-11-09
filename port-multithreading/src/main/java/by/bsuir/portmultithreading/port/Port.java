package by.bsuir.portmultithreading.port;

import java.util.Collections;
import java.util.PriorityQueue;

import by.bsuir.portmultithreading.ship.Ship;

public class Port {
    private class ProcessingThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (LOCK) {
                    while (shipsQueue.isEmpty()) {
                        try {
                            LOCK.wait();
                        }
                        catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    Ship ship = shipsQueue.peek();
                    if (ship.getOperation() == Ship.Operation.LOADING 
                            && !WAREHOUSE.isEnoughGoods(ship.getNeededGoods())) {
                        WAREHOUSE.orderMoreGoodsFromSuppliers();
                    }
                    if (isBerthAssigned(ship)) {
                        shipsQueue.remove(ship);
                    }
                }
            }
        }

        private boolean isBerthAssigned(Ship ship) {
            for (Berth berth : BERTHS) {
                if (!berth.isWorking()) {
                    berth.startBerthing(ship);
                    return true;
                }
            }

            return false;
        }
    }

    private static final Object LOCK = new Object();

    private final Warehouse WAREHOUSE = new Warehouse();
    private final Berth[] BERTHS;
    private PriorityQueue<Ship> shipsQueue = new PriorityQueue<>(Collections.reverseOrder());

    public Port() {
        BERTHS = new Berth[] {
            new Berth(WAREHOUSE),
            new Berth(WAREHOUSE),
            new Berth(WAREHOUSE),
            new Berth(WAREHOUSE),
            new Berth(WAREHOUSE)
        };
    }

    public void startProcessingShips() {
        new ProcessingThread().start();
    }

    public void addShip(Ship ship) {
        synchronized (LOCK) {
            shipsQueue.offer(ship);
            LOCK.notify();
        }
    }

}
