package beverage;

import java.util.LinkedList;

public class Beverage {
    public String name; // 음료 이름
    public int price; // 음료 가격
    public LinkedList<Integer> store; // 음료 재고 LinkedList

    public Beverage(String name, int price) { // 초기 재고 설정
        this.name = name;
        this.price = price;
        store = new LinkedList<>();
        store.add(3);
    }

    public String getName() { // 이름 가져오는 함수
        return name;
    }

    public int getPrice() { // 가격 가져오는 함수
        return price;
    }

    public int getStore() { // 재고량 가져오는 함수
        return store.getLast(); // 가장 최근 재고량 반환
    }

    public void addStore(int amount) { // 재고량 더하는 함수
        int currentStock = store.getLast();
        if (currentStock < 20) {
            store.add(currentStock + amount);
        }
    }

    public int buyBeverage() { // 재고량 빼는 함수
        if (store.getLast() > 0) {
            int currentStock = store.removeLast();
            store.add(currentStock - 1);
            return 0;
        } else {
            return 1;
        }
    }
}
