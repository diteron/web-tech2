package by.bsuir.portmultithreading.port;

import java.util.concurrent.atomic.AtomicInteger;

public class Warehouse {
    private AtomicInteger numberOfGoods = new AtomicInteger(200000);

    public boolean isEnoughGoods(int neededGoods) {
        int tmp = numberOfGoods.get();
        return tmp >= neededGoods;
    }

    public void orderMoreGoodsFromSuppliers() {
        numberOfGoods.addAndGet(50000);
    } 

    public void sendGoodsToBerth(int goodsQty) {
        numberOfGoods.addAndGet(-goodsQty);
    }

    public void recieveGoodsFromBerth(int goodsQty) {
        numberOfGoods.addAndGet(goodsQty);
    }

    public int getNuberOfGoods() {
        return numberOfGoods.get();
    }
}
