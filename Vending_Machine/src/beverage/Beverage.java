package beverage;

public class Beverage {
    public String name; // 음료 이름
    public int price; // 음료 가격
    public int store; // 음료 재고
    public Beverage(String name, int price){
        this.name = name;
        this.price = price;
        store = 3;
    }
    public String getName(){ // 이름 가져오는 함수
        return name;
    }
    public int getPrice(){ // 가격 가져오는 함수
        return price;
    }
    public int getStore(){ // 재고량 가져오는 함수
        return store;
    }

    public void addStore(int amount){ // 재고량 더하는 함수
        if (store < 20) {
            store+=amount;
        }
    }
    public int buyBeverage(){ // 재고량 빼는 함수
        if (store > 0) {
            store--;
            return 0;
        } else {
            return 1;
        }
    }
}
