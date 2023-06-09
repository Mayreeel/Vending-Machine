package vendingmachine;

import beverage.Beverage;

import java.util.ArrayList;

public interface IVendingMachine {
    void insertMoney(int unit);// 돈을 집어넣음 -> 구매할 수 있는 음료들의 불이 켜져야함

    void buyBeverage(ArrayList<Beverage> beverages, String name);// 음료를 구매
    void returnChange(); // 잔돈을 반환
    void accessAdminMenu(String password); // 관리자 메뉴 접근
}
