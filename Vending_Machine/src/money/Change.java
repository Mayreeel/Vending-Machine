package money;

public class Change {
    Coin[] coins;
    Bill[] bills;
    public Change(){ // 거스름돈 초기화
        coins = new Coin[4];
        coins[0] = new Coin(10);
        coins[1] = new Coin(50);
        coins[2] = new Coin(100);
        coins[3] = new Coin(500);

        bills = new Bill[1];
        bills[0] = new Bill(1000);
    }

    public Coin[] getCoins() {
        return coins;
    }

    public Bill[] getBills() {
        return bills;
    }

    public Coin findCoin(int unit){ // 화폐 찾기
        for(Coin coin : coins){
            if (coin.getUnit() == unit) {
                return coin;
            }
        }
        return null;
    }

    public Bill findBill(int unit){ // 지폐 찾기
        for(Bill bill : bills){
            if (bill.getUnit() == unit) {
                return bill;
            }
        }
        return null;
    }

}
