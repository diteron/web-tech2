package by.bsuir.portmultithreading.port;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import by.bsuir.portmultithreading.ship.Ship;
import by.bsuir.portmultithreading.ship.Ship.Operation;

public class Berth {
    private class BerthThread extends Thread {
        private final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

        @Override
        public void run() {
            if (operation == Operation.LOADING) {
                WAREHOUSE.sendGoodsToBerth(berthingShip.getNeededGoods());
            }

            System.out.println(LocalTime.now().format(TIME_FORMAT) + ":  " + "Ship with id " + berthingShip.getId() + ", " 
                    + "ship priority " + berthingShip.getPriority() + ", "
                    + "cargo priority " + berthingShip.getCargoPriority() + ", "
                    + "has started " + operation + " on berth " + id + ", "
                    + "specified duration of berthing " + specifiedTimeOfBerthing + " ms");
            
            // Loading/unloading ship
            try {
                sleep(berthingShip.getActualBerthingTime());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(LocalTime.now().format(TIME_FORMAT) + ":  " + "Ship with id " + berthingShip.getId() + ", " 
                    + "ship priority " + berthingShip.getPriority() + ", "
                    + "cargo priority " + berthingShip.getCargoPriority() + ", "
                    + "has completed " + operation + " on berth " + id + ", "
                    + "actual duration of berthing " + berthingShip.getActualBerthingTime() + " ms");

            actualTimeOfBerthing = berthingShip.getActualBerthingTime();

            if (operation == Operation.UNLOADING) {
                WAREHOUSE.recieveGoodsFromBerth(berthingShip.getNumberOfCargo());
            }

            if (isBerthingTimeExceeded()) {
                berthingShip.decreasePriority();    // Punish ship
            }
            else {
                berthingShip.increasePriority();    // Reward ship
            }

            berthingShip.finishWorking();
            isWorking = false;
        }
    }

    private static Integer berthsCounter = 0;
    private Integer id;

    private boolean isWorking = false;

    private Ship berthingShip;
    private Operation operation;

    private Long specifiedTimeOfBerthing = 0L;
    private Long actualTimeOfBerthing = 0L;

    private final Warehouse WAREHOUSE;

    public Berth (Warehouse warehouse) {
        id = ++berthsCounter;
        WAREHOUSE = warehouse;
    }

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
    
    private boolean isBerthingTimeExceeded() {
        return actualTimeOfBerthing > specifiedTimeOfBerthing;
    }

    public Ship getBerthingShip() {
        return berthingShip;
    }
    
}
