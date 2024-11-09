package by.bsuir.portmultithreading.port;

public class Warehouse {
    private int numberOfGoods = 200000;

    public synchronized  boolean isEnoughGoods(int neededGoods) {
        return numberOfGoods >= neededGoods;
    }

    public synchronized void orderMoreGoodsFromSuppliers() {
        numberOfGoods += 50000;
    } 

    public synchronized void sendGoodsToBerth(int goodsQty) {
        numberOfGoods -= goodsQty;
    }

    public synchronized void recieveGoodsFromBerth(int goodsQty) {
        numberOfGoods += goodsQty;
    }

    public synchronized int getNuberOfGoods() {
        return numberOfGoods;
    }
}
