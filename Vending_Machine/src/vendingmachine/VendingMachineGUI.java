package vendingmachine;

import beverage.Beverage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VendingMachineGUI {

    static VendingMachine vendingMachine;
    static ArrayList<Beverage> beverages;
    static JPanel panel = new JPanel(new GridBagLayout());
    static Map<JButton, Integer> map = new HashMap<>();
    static JLabel moneyInputLabel;
    static JLabel changeOutputLabel;
    static JComboBox<String> coinComboBox;
    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VendingMachineGUI::createAndShowGUI);
    }

    /**
     * GUI 생성 및 표시 메서드
     */
    private static void createAndShowGUI() {
        vendingMachine = new VendingMachine();
        beverages = vendingMachine.initBeverage();
        JFrame frame = new JFrame("자판기 관리 프로그램");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 300);

        GridBagConstraints constraints = new GridBagConstraints();

        for (int i = 0; i < beverages.size(); i++) {
            Beverage beverage = beverages.get(i);

            constraints.gridx = i;
            constraints.gridy = 0;
            JLabel beverageName = new JLabel(beverage.getName());
            panel.add(beverageName, constraints);

            constraints.gridy = 1;
            JLabel priceLabel = new JLabel("가격: ₩" + beverage.getPrice());
            panel.add(priceLabel, constraints);

            constraints.gridy = 2;
            JLabel stockLabel = new JLabel("재고량: " + beverage.getStore());
            panel.add(stockLabel, constraints);

            constraints.gridy = 3;
            JButton purchaseButton = new JButton("구매");
            panel.add(purchaseButton, constraints);

            map.put(purchaseButton, i);

            purchaseButton.addActionListener(e -> {
                int index = map.get(purchaseButton);
                Beverage beverage1 = beverages.get(index);
                executorService.execute(() -> {
                    vendingMachine.buyBeverage(beverages, beverage1.getName());
                    SwingUtilities.invokeLater(() -> {
                        stockLabel.setText("재고량: " + beverage1.getStore());
                        refreshPurchaseButtons();
                    });
                });
            });

            if (vendingMachine.getInsertedMoney() < beverage.getPrice() || beverage.getStore() == 0) {
                purchaseButton.setEnabled(false);
            }
        }

        constraints.gridx = 0;
        constraints.gridy = 4;
        moneyInputLabel = new JLabel("돈 투입");
        panel.add(moneyInputLabel, constraints);

        constraints.gridy = 5;
        JButton billInputButton = new JButton("지폐 투입구");
        panel.add(billInputButton, constraints);

        billInputButton.addActionListener(e -> executorService.execute(() -> {
            vendingMachine.insertMoney(1000);
            SwingUtilities.invokeLater(VendingMachineGUI::refreshPurchaseButtons);
        }));

        constraints.gridy = 6;
        JButton coinInputButton = new JButton("동전 투입구");
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
                        executorService.execute(() -> {
                            vendingMachine.insertMoney(coinAmount);
                            SwingUtilities.invokeLater(VendingMachineGUI::refreshPurchaseButtons);
                        });
                    }
                }
            } catch (NumberFormatException ex) {
                // 숫자 변환 예외 처리
            }
        });

        constraints.gridx = 4;
        constraints.gridy = 4;
        JButton changeReturnButton = new JButton("거스름돈 반환");
        changeReturnButton.addActionListener(e -> executorService.execute(() -> {
            vendingMachine.returnChange();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "잔돈 반환 완료", "잔돈 반환", JOptionPane.INFORMATION_MESSAGE);
                refreshPurchaseButtons();
                moneyInputLabel.setText("들어간 돈: " + vendingMachine.getInsertedMoney()+" 원");
            });
        }));
        panel.add(changeReturnButton, constraints);

        constraints.gridy = 5;
        JLabel beverageOutput = new JLabel();
        panel.add(beverageOutput, constraints);

        constraints.gridy = 6;
        JLabel changeOutput = new JLabel();
        panel.add(changeOutput, constraints);

        constraints.gridy = 7;
        changeOutputLabel = new JLabel();
        panel.add(changeOutputLabel, constraints);

        constraints.gridx = 4;
        constraints.gridy = 5;
        JButton adminMenuButton = new JButton("관리자 메뉴");
        panel.add(adminMenuButton, constraints);

        adminMenuButton.addActionListener(e -> {
            String password = JOptionPane.showInputDialog("패스워드를 입력하세요:");
            if (password != null && !password.isEmpty()) {
                vendingMachine.accessAdminMenu(password);
            } else {
                JOptionPane.showMessageDialog(null, "패스워드가 맞지 않습니다.", "접근 거부", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    /**
     * 구매 버튼을 새로고침하는 메서드
     */
    static void refreshPurchaseButtons() {
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton button) {
                String text = button.getText();
                if (text.equals("구매")) {
                    int index = map.get(button);
                    Beverage beverage = beverages.get(index);
                    if (beverage != null) {
                        button.setEnabled(vendingMachine.getInsertedMoney() >= beverage.getPrice() && beverage.getStore() != 0);
                        JLabel stockLabel = (JLabel) panel.getComponent(index * 4 + 2);
                        stockLabel.setText("재고량: " + beverage.getStore());
                    }
                }
            }
        }
        moneyInputLabel.setText("들어간 돈: " + vendingMachine.getInsertedMoney()+" 원");
    }
}
