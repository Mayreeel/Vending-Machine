package money;

public class Bill extends AbsMoney {
    public Bill(int unit) {
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