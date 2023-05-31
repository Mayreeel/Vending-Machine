public class Main {
    public static void main(String[] args) {
        Change change = new Change();
        MoneyInput moneyInput = new MoneyInput(change);

        moneyInput.insertCoin(500);
        moneyInput.insertCoin(1000);
        moneyInput.insertCoin(50);
        moneyInput.insertCoin(100);
        moneyInput.insertCoin(5000);  // 유효하지 않은 화폐 단위

        System.out.println("총 입력 금액: " + moneyInput.getTotalAmount());
    }
}