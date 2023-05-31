public class Beverage {
    private String name;
    private int price;
    private int stock;
    private boolean soldOut;

    public Beverage(String name, int price) {
        this.name = name;
        this.price = price;
        this.stock = 3;
        this.soldOut = false;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void replenishStock(int amount) {
        stock += amount;
    }

    public void decreaseStock() throws SoldOutException {
        if (stock > 0) {
            stock--;
        } else {
            throw new SoldOutException("재고가 없습니다.");
        }
    }


    // 추가적인 메서드 구현 등

    @Override
    public String toString() {
        return "Beverage{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", soldOut=" + soldOut +
                '}';
    }

    public static void main(String[] args) {
        Beverage[] beverages = new Beverage[5];
        beverages[0] = new Beverage("물", 450);
        beverages[1] = new Beverage("커피", 500);
        beverages[2] = new Beverage("이온음료", 550);
        beverages[3] = new Beverage("고급커피", 700);
        beverages[4] = new Beverage("탄산음료", 750);

        // 각 음료의 정보 출력
        for (Beverage beverage : beverages) {
            System.out.println(beverage);
        }

        // 음료 1개씩 판매
        beverages[0].decreaseStock();
        beverages[1].decreaseStock();
        beverages[2].decreaseStock();
        beverages[3].decreaseStock();
        beverages[4].decreaseStock();

        // 각 음료의 정보 출력 (판매 후)
        for (Beverage beverage : beverages) {
            System.out.println(beverage);
        }
    }
}