public abstract class AbsCoin {
    protected int denomination;
    protected int stock;

    public AbsCoin(int denomination, int stock) {
        this.denomination = denomination;
        this.stock = stock;
    }

    public int getDenomination() {
        return denomination;
    }

    public int getStock() {
        return stock;
    }

    public void decreaseStock(int count) {
        if (stock >= count) {
            stock -= count;
        } else {
            // 재고 부족 처리 로직
        }
    }
}