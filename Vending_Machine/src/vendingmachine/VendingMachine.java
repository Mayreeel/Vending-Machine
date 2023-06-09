package vendingmachine;

import beverage.Beverage;
import money.Bill;
import money.Change;
import money.Coin;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

import static vendingmachine.VendingMachineGUI.*;

public class VendingMachine implements IVendingMachine {
    Change change = new Change();
    int insertedMoney = 0;

    // 스택과 큐 선언
    Stack<Integer> changeStack = new Stack<>();
    Queue<Integer> insertedBillQueue = new LinkedList<>();

    /**
     * 사용자가 투입한 돈의 총액을 반환하는 메서드
     */
    public int getInsertedMoney() {
        return insertedMoney;
    }

    /**
     * 음료 초기화 메서드
     */
    public ArrayList<Beverage> initBeverage() {
        ArrayList<Beverage> beverages = new ArrayList<>();
        Beverage water = new Beverage("물", 450);
        Beverage coffee = new Beverage("커피", 500);
        Beverage sportsDrink = new Beverage("이온음료", 550);
        Beverage premiumCoffee = new Beverage("고급커피", 700);
        Beverage softDrink = new Beverage("탄산음료", 750);
        beverages.add(water);
        beverages.add(coffee);
        beverages.add(sportsDrink);
        beverages.add(premiumCoffee);
        beverages.add(softDrink);
        return beverages;
    }

    @Override
    public void insertMoney(int unit) {
        if (unit == 1000) {
            if (insertedBillQueue.size() < 3 && insertedMoney + unit <= 5000) {
                Bill bill = change.findBill(unit);
                bill.increaseStore(1);
                insertedBillQueue.add(1);
                insertedMoney += unit;

                // 돈을 보여주는 라벨 업데이트
                moneyInputLabel.setText("들어간 돈: " + insertedMoney + " 원");
            } else {
                // 입력 한도를 넘긴 경우 경고 메시지 표시
                JOptionPane.showMessageDialog(null, "입력 한도를 넘었습니다. 지폐 반환: " + unit + "원", "경고", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            if (insertedMoney + unit <= 5000) {
                Coin coin = change.findCoin(unit);
                coin.increaseStore(1);
                insertedMoney += unit;

                // 돈을 보여주는 라벨 업데이트
                moneyInputLabel.setText("들어간 돈: " + insertedMoney + " 원");
            } else {
                // 입력 한도를 넘긴 경우 경고 메시지 표시
                JOptionPane.showMessageDialog(null, "입력 한도를 넘었습니다. 반환: " + unit + "원", "경고", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * 음료를 찾는 메서드
     */
    public Beverage findBeverage(ArrayList<Beverage> beverages, String name) {
        for (Beverage beverage : beverages) {
            if (Objects.equals(beverage.name, name)) {
                return beverage;
            }
        }
        return null;
    }

    @Override
    public void buyBeverage(ArrayList<Beverage> beverages, String name) {
        Beverage beverage = findBeverage(beverages, name);
        int result = beverage.buyBeverage();
        if (result == 0) {
            insertedMoney -= beverage.price;
            // 구매 결과를 반영
            JOptionPane.showMessageDialog(null, "구매 성공\n음료: " + beverage.getName(), "구매 결과", JOptionPane.INFORMATION_MESSAGE);
            // 매출 데이터 저장
            saveSalesData(beverage.getName());
        } else {
            // 구매 실패 메시지를 반영
            JOptionPane.showMessageDialog(null, "구매 실패\n음료: " + beverage.getName(), "구매 결과", JOptionPane.WARNING_MESSAGE);
        }

        // 구매 후 상태를 업데이트
        if (insertedMoney == 0) {
            insertedBillQueue.clear();
        }

        // 구매 버튼 상태 업데이트
        refreshPurchaseButtons();
    }

    /**
     * 음료 재고를 보충하는 메서드
     */
    public void replenishBeverageStock(Beverage beverage, int quantity) {
        if (beverage != null) {
            beverage.addStore(quantity);
        }
    }

    /**
     * 매출 데이터를 저장하는 메서드
     */
    public void saveSalesData(String beverageName) {
        try {
            File file = new File("sales.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            // 현재 날짜 가져오기
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(new Date());

            // 매출 데이터 작성
            writer.write(date + "," + beverageName);
            writer.newLine();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 잔돈을 반환하는 메서드
     */
    public void returnChange() {
        int remainingMoney = insertedMoney;
        StringBuilder changeOutputText = new StringBuilder();

        // 지폐 반환
        while (!changeStack.isEmpty()) {
            int billValue = changeStack.pop();
            int billsToReturn = Math.min(remainingMoney / billValue, change.findBill(billValue).getStore());

            if (billsToReturn > 0) {
                int returnedMoney = billValue * billsToReturn;
                remainingMoney -= returnedMoney;
                changeOutputText.append(billValue).append("원 지폐: ").append(billsToReturn).append("장\n");
                change.findBill(billValue).decreaseStore(billsToReturn);
            }
        }

        // 동전 반환
        for (int i = change.getCoins().length - 1; i >= 0; i--) {
            Coin coin = change.getCoins()[i];
            int coinValue = coin.getUnit();
            int coinsToReturn = Math.min(remainingMoney / coinValue, coin.getStore());

            if (coinsToReturn > 0) {
                int returnedMoney = coinValue * coinsToReturn;
                remainingMoney -= returnedMoney;
                changeOutputText.append(coinValue).append("원 동전: ").append(coinsToReturn).append("개\n");
                coin.decreaseStore(coinsToReturn);
            }
        }

        if (remainingMoney != 0) {
            changeOutputText.append("잔돈 반환 불가, 남은 금액: ₩").append(remainingMoney).append("\n");
            changeOutputText.append("관리자를 호출하겠습니다. 잠시만 기다려주십시오.");
        } else {
            insertedMoney = 0;
            insertedBillQueue.clear();
        }

        // 줄바꿈 처리
        String changeOutput = changeOutputText.toString().replace("\n", "<br>");

        // 잔돈 반환 결과를 업데이트
        changeOutputLabel.setText("<html>" + changeOutput + "</html>");
    }

    /**
     * 관리자 메뉴에 접근하는 메서드
     */
    public void accessAdminMenu(String password) {
        if (password.equals("password")) {
            AdminMenu adminMenu = new AdminMenu(change);
            adminMenu.showMenu();
        } else {
            JOptionPane.showMessageDialog(null, "비밀번호가 다릅니다.", "관리자 메뉴", JOptionPane.ERROR_MESSAGE);
        }
    }
}
