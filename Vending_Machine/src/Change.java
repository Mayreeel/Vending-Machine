public class Change {
    private Coin[] coins;

    public Change() {
        coins = new Coin[5];
        coins[0] = new Coin(10);
        coins[1] = new Coin(50);
        coins[2] = new Coin(100);
        coins[3] = new Coin(500);
        coins[4] = new Coin(1000);
    }

    public Coin[] getCoins() {
        return coins;
    }

    public void decreaseCoinStock(int denomination) throws InvalidDenominationException {
        Coin coin = findCoin(denomination);
        if (coin != null) {
            coin.decreaseStock(denomination);
        } else {
            throw new InvalidDenominationException("유효하지 않은 화폐 단위입니다.");
        }
    }

    public Coin findCoin(int denomination) {
        for (Coin coin : coins) {
            if (coin.getDenomination() == denomination) {
                return coin;
            }
        }
        return null;
    }

    public int[] calculateChange(int amount) throws InsufficientChangeException {
        int[] change = new int[coins.length];
        int remainingAmount = amount;

        for (int i = coins.length - 1; i >= 0; i--) {
            Coin coin = coins[i];
            int coinDenomination = coin.getDenomination();
            int coinCount = Math.min(remainingAmount / coinDenomination, coin.getStock());
            remainingAmount -= coinDenomination * coinCount;
            coin.decreaseStock(coinCount);
            change[i] = coinCount;
        }

        if (remainingAmount != 0) {
            throw new InsufficientChangeException("화폐가 부족합니다.");
        }

        return change;
    }

    // 추가적인 메서드 구현 등
}
