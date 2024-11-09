package by.bsuir.portmultithreading;

import by.bsuir.portmultithreading.port.Port;
import by.bsuir.portmultithreading.ship.ShipsManager;

public class Application {
    public static void main(String[] args) {
        Port port = new Port(5);
        ShipsManager shipsManager = new ShipsManager(20);
        shipsManager.setPort(port);
        port.startProcessingShips();
        shipsManager.startSendingShips();
    }
}