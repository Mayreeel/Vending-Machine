public class Coin extends AbsCoin {
    public Coin(int denomination) {
        super(denomination, 5);
    }

    public void increaseStock(int count) {
        stock += count;
    }
}