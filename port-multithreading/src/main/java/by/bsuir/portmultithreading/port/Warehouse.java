package by.bsuir.portmultithreading.port;

public class Warehouse {
    private Integer numberOfGoods = 1500000;

    public synchronized boolean isEnoughGoods(Integer neededGoods) {
        return numberOfGoods >= neededGoods;
    }

    public synchronized void orderMoreGoodsFromSuppliers() {
        numberOfGoods += 50000;
    } 

    public synchronized void sendGoodsToBerth(Integer goodsQty) {
        numberOfGoods -= goodsQty;
    }

    public synchronized void recieveGoodsFromBerth(Integer goodsQty) {
        numberOfGoods += goodsQty;
    }
}
