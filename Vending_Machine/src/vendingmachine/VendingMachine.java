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
import java.util.Objects;
import java.util.Date;

import static vendingmachine.VendingMachineGUI.*;

public class VendingMachine implements IVendingMachine {
    Change change = new Change();
    int insertedMoney = 0;

    public int getInsertedMoney() {
        return insertedMoney;
    }

    ArrayList<Integer> insertedBill = new ArrayList<>();


    public ArrayList<Beverage> initBeverage() { // 음료 초기화
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
        return(beverages);
    }

    @Override
    public void beverageList(ArrayList<Beverage> beverages) { // 음료 리스트를 보여줌
        for (Beverage beverage : beverages){
            if (beverage.store!=0){
                System.out.println(beverage.name + beverage.price + "판매 중" + beverage.store + "개");
            } else{
                System.out.println(beverage.name + beverage.price + "품절" + beverage.store + "개");
            }
        }
    }

    @Override
    public void beverageList(ArrayList<Beverage> beverages, int insertedMoney) { // 음료 리스트를 넣은 돈에 따라 다르게 보여줌
        for (Beverage beverage : beverages) {
            if (beverage.store != 0) {
                String status;
                if (insertedMoney != 0) {
                    status = (beverage.price <= insertedMoney) ? "구매 가능" : "구매 불가";
                } else {
                    status = "판매 중";
                }
                System.out.println(beverage.name + beverage.price + status);
            } else {
                System.out.println(beverage.name + beverage.price + "품절");
            }
        }
    }

    @Override
    public void insertMoney(int unit) {
        if (unit == 1000) {
            if (insertedBill.size() < 3 && insertedMoney + unit <= 5000) {
                Bill bill = change.findBill(unit);
                bill.increaseStore(1);
                insertedBill.add(1);
                insertedMoney += unit;

                // 돈을 보여주는 라벨 업데이트
                moneyInputLabel.setText("Insert Money: $" + insertedMoney);
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
                moneyInputLabel.setText("Insert Money: $" + insertedMoney);
            } else {
                // 입력 한도를 넘긴 경우 경고 메시지 표시
                JOptionPane.showMessageDialog(null, "입력 한도를 넘었습니다. 동전 반환: " + unit + "원", "경고", JOptionPane.WARNING_MESSAGE);
            }
        }
    }


    public Beverage findBeverage(ArrayList<Beverage> beverages, String name) {
        for (Beverage beverage : beverages){
            if(Objects.equals(beverage.name, name)){
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
            System.out.println("구매 성공: " + beverage.getName());
            insertedMoney -= beverage.price;
            // 구매 결과를 반영
            JOptionPane.showMessageDialog(null, "구매 성공\n음료: " + beverage.getName(), "구매 결과", JOptionPane.INFORMATION_MESSAGE);
            // 매출 데이터 저장
            saveSalesData(beverage.getName());
        } else {
            System.out.println("구매 실패: " + beverage.getName());
            // 구매 실패 메시지를 반영
            JOptionPane.showMessageDialog(null, "구매 실패\n음료: " + beverage.getName(), "구매 결과", JOptionPane.WARNING_MESSAGE);
        }


        // 구매 후 상태를 업데이트
        if (insertedMoney != 0) {
            beverageList(beverages, insertedMoney);
        } else {
            beverageList(beverages);
            insertedBill.clear();
        }

        // 구매 버튼 상태 업데이트
        refreshPurchaseButtons();
    }

    public void replenishBeverageStock(Beverage beverage, int quantity) {
        if (beverage != null) {
            beverage.addStore(quantity);
        }
    }

    public void saveSalesData(String beverageName) {
        try {
            File file = new File("sales.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            // Get current date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(new Date());

            // Write sales data
            writer.write(date + "," + beverageName);
            writer.newLine();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void returnChange() {
        int remainingMoney = insertedMoney;
        StringBuilder changeOutputText = new StringBuilder();

        // 지폐 반환
        for (int i = change.getBills().length - 1; i >= 0; i--) {
            Bill bill = change.getBills()[i];
            int billValue = bill.getUnit();
            int billsToReturn = Math.min(remainingMoney / billValue, bill.getStore());

            if (billsToReturn > 0) {
                int returnedMoney = billValue * billsToReturn;
                remainingMoney -= returnedMoney;
                changeOutputText.append("$").append(billValue).append(" Bills: ").append(billsToReturn).append("개\n");
                bill.decreaseStore(billsToReturn);
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
                changeOutputText.append("$").append(coinValue).append(" Coins: ").append(coinsToReturn).append("개\n");
                coin.decreaseStore(coinsToReturn);
            }
        }

        if (remainingMoney != 0) {
            changeOutputText.append("잔돈 반환 불가, 남은 금액: $").append(remainingMoney).append("\n");
            changeOutputText.append("관리자를 호출하겠습니다. 잠시만 기다려주십시오.");
        } else {
            insertedMoney = 0;
            insertedBill.clear();
        }

        // 줄바꿈 처리
        String changeOutput = changeOutputText.toString().replace("\n", "<br>");

        // 잔돈 반환 결과를 업데이트
        changeOutputLabel.setText("<html>" + changeOutput + "</html>");
    }

    public void accessAdminMenu(String password) {
        if (password.equals("1")) {
            AdminMenu adminMenu = new AdminMenu(change);
            adminMenu.showMenu();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid password!", "Admin Menu", JOptionPane.ERROR_MESSAGE);
        }
    }

}
