package money;

public class Coin extends AbsMoney {
    public Coin(int unit) {
        super(unit, 5);
    }

    @Override
    public int getUnit() {
        return super.getUnit();
    }

    @Override
    public int getStore() {
        return super.getStore();
    }

    public void increaseStore(int count) {
        store += count;
    }

    public void decreaseStore(int count) {
        store -= count;
    }
}
