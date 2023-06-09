package vendingmachine;

import beverage.Beverage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VendingMachineGUI {

    static VendingMachine vendingMachine;
    static ArrayList<Beverage> beverages;
    static JPanel panel = new JPanel(new GridBagLayout());
    static Map<JButton, Integer> map = new HashMap<>();
    static JLabel moneyInputLabel;
    static JLabel changeOutputLabel;
    static JComboBox<String> coinComboBox;

    public static void main(String[] args) {
        vendingMachine = new VendingMachine();
        beverages = vendingMachine.initBeverage();
        JFrame frame = new JFrame("Vending Machine");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        GridBagConstraints constraints = new GridBagConstraints();

        for (int i = 0; i < beverages.size(); i++) {
            Beverage beverage = beverages.get(i);

            constraints.gridx = i;
            constraints.gridy = 0;
            JLabel beverageName = new JLabel(beverage.getName());
            panel.add(beverageName, constraints);

            constraints.gridy = 1;
            JLabel priceLabel = new JLabel("Price: $" + beverage.getPrice());
            panel.add(priceLabel, constraints);

            constraints.gridy = 2;
            JLabel stockLabel = new JLabel("Stock: " + beverage.getStore());
            panel.add(stockLabel, constraints);

            constraints.gridy = 3;
            JButton purchaseButton = new JButton("Purchase");
            panel.add(purchaseButton, constraints);

            map.put(purchaseButton, i);

            purchaseButton.addActionListener(e -> {
                int index = map.get(purchaseButton);
                Beverage beverage1 = beverages.get(index);
                vendingMachine.buyBeverage(beverages, beverage1.getName());
                stockLabel.setText("Stock: " + beverage1.getStore());
                refreshPurchaseButtons();
            });

            if (vendingMachine.getInsertedMoney() < beverage.getPrice() || beverage.getStore() == 0) {
                purchaseButton.setEnabled(false);
            }
        }

        constraints.gridx = 0;
        constraints.gridy = 4;
        moneyInputLabel = new JLabel("Insert Money");
        panel.add(moneyInputLabel, constraints);

        constraints.gridy = 5;
        JButton billInputButton = new JButton("Insert Bill");
        panel.add(billInputButton, constraints);

        billInputButton.addActionListener(e -> {
            vendingMachine.insertMoney(1000);
            refreshPurchaseButtons();
        });

        constraints.gridy = 6;
        JButton coinInputButton = new JButton("Insert Coin");
        panel.add(coinInputButton, constraints);

        coinInputButton.addActionListener(e -> {
            try {
                String[] coinOptions = {"10", "50", "100", "500"};
                coinComboBox = new JComboBox<>(coinOptions);

                int option = JOptionPane.showOptionDialog(
                        null,
                        coinComboBox,
                        "동전 선택",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null
                );

                if (option == JOptionPane.OK_OPTION) {
                    String selectedCoin = (String) coinComboBox.getSelectedItem();
                    if (selectedCoin != null) {
                        int coinAmount = Integer.parseInt(selectedCoin);
                        vendingMachine.insertMoney(coinAmount);
                        refreshPurchaseButtons();
                    }
                }
            } catch (NumberFormatException ex) {
                // 숫자 변환 예외 처리
            }
        });

        constraints.gridx = 4;
        constraints.gridy = 4;
        JButton changeReturnButton = new JButton("Return Change");
        changeReturnButton.addActionListener(e -> {
            vendingMachine.returnChange();
            // 잔돈 반환 결과를 반영
            JOptionPane.showMessageDialog(null, "잔돈 반환 완료", "잔돈 반환", JOptionPane.INFORMATION_MESSAGE);
            refreshPurchaseButtons();
            moneyInputLabel.setText("Insert Money: $" + vendingMachine.getInsertedMoney());
        });
        panel.add(changeReturnButton, constraints);

        constraints.gridy = 5;
        JLabel beverageOutput = new JLabel();
        panel.add(beverageOutput, constraints);

        constraints.gridy = 6;
        JLabel changeOutput = new JLabel();
        panel.add(changeOutput, constraints);

        // 잔돈 반환 결과를 표시할 라벨 생성
        constraints.gridy = 7;
        changeOutputLabel = new JLabel();
        panel.add(changeOutputLabel, constraints);

        //Admin Menu 버튼
        constraints.gridx = 4;
        constraints.gridy = 5;
        JButton adminMenuButton = new JButton("Admin Menu");
        panel.add(adminMenuButton, constraints);

        adminMenuButton.addActionListener(e -> {
            String password = JOptionPane.showInputDialog("Enter password:");
            if (password != null && !password.isEmpty()) {
                vendingMachine.accessAdminMenu(password);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid password", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    static void refreshPurchaseButtons() {
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton button) {
                String text = button.getText();
                if (text.equals("Purchase")) {
                    int index = map.get(button);
                    Beverage beverage = beverages.get(index);
                    if (beverage != null) {
                        button.setEnabled(vendingMachine.getInsertedMoney() >= beverage.getPrice() && beverage.getStore() != 0);
                        // 재고를 갱신하여 표시
                        JLabel stockLabel = (JLabel) panel.getComponent(index * 4 + 2);
                        stockLabel.setText("Stock: " + beverage.getStore());
                    }
                }
            }
        }
        moneyInputLabel.setText("Insert Money: $" + vendingMachine.getInsertedMoney());
    }
}
