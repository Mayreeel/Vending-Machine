public class MoneyInput {
    private Change change;
    private int totalAmount;

    public MoneyInput(Change change) {
        this.change = change;
        totalAmount = 0;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void insertCoin(int denomination) {
        Coin coin = change.findCoin(denomination);
        if (coin != null) {
            totalAmount += denomination;
            change.decreaseCoinStock(denomination);
        } else {
            System.out.println("유효하지 않은 화폐 단위입니다.");
        }
    }

    public void resetTotalAmount() {
        totalAmount = 0;
    }
}