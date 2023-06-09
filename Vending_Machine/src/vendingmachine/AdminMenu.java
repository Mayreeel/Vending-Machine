package vendingmachine;

import beverage.Beverage;
import money.Bill;
import money.Change;
import money.Coin;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static vendingmachine.VendingMachineGUI.beverages;
import static vendingmachine.VendingMachineGUI.vendingMachine;

public class AdminMenu {
    private final Change change;
    private final Map<String, Integer> salesByMonth;
    private final Map<String, Integer> salesByDate;
    private final Map<String, Map<String, Integer>> salesByBeverage;

    public AdminMenu(Change change) {
        this.change = change;
        this.salesByMonth = new HashMap<>();
        this.salesByDate = new HashMap<>();
        this.salesByBeverage = new HashMap<>();
    }

    /**
     * 관리자 메뉴를 표시하는 메서드
     */
    public void showMenu() {
        JFrame frame = new JFrame("관리자 메뉴");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("버튼을 선택하세요");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        JButton monthlySalesButton = new JButton("월/일 매출 계산");
        monthlySalesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        monthlySalesButton.addActionListener(e -> calculateMonthlySales());
        panel.add(monthlySalesButton);

        JButton salesByBeverageButton = new JButton("음료별 월/일 매출 계산");
        salesByBeverageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        salesByBeverageButton.addActionListener(e -> calculateSalesByBeverage());
        panel.add(salesByBeverageButton);

        JButton replenishBeverageStockButton = new JButton("음료 재고 보충");
        replenishBeverageStockButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        replenishBeverageStockButton.addActionListener(e -> replenishBeverageStock());
        panel.add(replenishBeverageStockButton);

        JButton replenishChangeStockButton = new JButton("잔돈 재고 보충");
        replenishChangeStockButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        replenishChangeStockButton.addActionListener(e -> replenishChangeStock());
        panel.add(replenishChangeStockButton);

        JButton checkChangeStockButton = new JButton("잔돈 재고 확인");
        checkChangeStockButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkChangeStockButton.addActionListener(e -> checkChangeStock());
        panel.add(checkChangeStockButton);

        JButton collectChangeButton = new JButton("잔돈 수금");
        collectChangeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        collectChangeButton.addActionListener(e -> collectChange());
        panel.add(collectChangeButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     * 잔돈을 수금하는 메서드
     */
    private void collectChange() {
        int totalChange = 0;
        StringBuilder changeInfo = new StringBuilder();

        // 지폐 수금
        for (Bill bill : change.getBills()) {
            int billCount = bill.getStore();
            if (billCount > 3) {
                billCount -= 3;
                bill.decreaseStore(billCount);
                totalChange += bill.getUnit() * billCount;
                changeInfo.append(bill.getUnit()).append("원 지폐: ").append(billCount).append("\n");
            }
        }

        // 동전 수금
        for (Coin coin : change.getCoins()) {
            int coinCount = coin.getStore();
            if (coinCount > 3) {
                coinCount -= 3;
                coin.decreaseStore(coinCount);
                totalChange += coin.getUnit() * coinCount;
                changeInfo.append(coin.getUnit()).append("원 동전: ").append(coinCount).append("\n");
            }
        }

        JOptionPane.showMessageDialog(null, "수금 완료\n총 : " + totalChange + "원\n\n나온 돈:\n" + changeInfo, "나온 돈", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 잔돈 재고를 확인하는 메서드
     */
    private void checkChangeStock() {
        StringBuilder stockInfo = new StringBuilder();
        stockInfo.append("잔돈 재고:\n");

        // 지폐 재고 확인
        for (Bill bill : change.getBills()) {
            stockInfo.append(bill.getUnit()).append("원 지폐: ").append(bill.getStore()).append("\n");
        }

        // 동전 재고 확인
        for (Coin coin : change.getCoins()) {
            stockInfo.append(coin.getUnit()).append("원 동전: ").append(coin.getStore()).append("\n");
        }

        JOptionPane.showMessageDialog(null, stockInfo.toString(), "잔돈 변경", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 음료 재고를 보충하는 메서드
     */
    private void replenishBeverageStock() {
        JComboBox<String> beverageComboBox = new JComboBox<>();
        for (Beverage beverage : beverages) {
            beverageComboBox.addItem(beverage.getName());
        }

        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        JSpinner quantitySpinner = new JSpinner(spinnerModel);

        Object[] message = {
                "음료:", beverageComboBox,
                "개수:", quantitySpinner
        };

        int option = JOptionPane.showOptionDialog(null, message, "음료 재고 보충", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (option == JOptionPane.OK_OPTION) {
            String selectedBeverageName = (String) beverageComboBox.getSelectedItem();
            int quantity = (int) quantitySpinner.getValue();

            Beverage selectedBeverage = vendingMachine.findBeverage(beverages, selectedBeverageName);
            if (selectedBeverage != null) {
                vendingMachine.replenishBeverageStock(selectedBeverage, quantity);
                JOptionPane.showMessageDialog(null, "음료 보충 " + selectedBeverage.getName() + " " + quantity + "개", "음료 재고 보충", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "음료 찾기 실패!", "음료 재고 보충", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 잔돈 재고를 보충하는 메서드
     */
    private void replenishChangeStock() {
        JComboBox<String> denominationComboBox = new JComboBox<>();

        denominationComboBox.addItem("₩10");
        denominationComboBox.addItem("₩50");
        denominationComboBox.addItem("₩100");
        denominationComboBox.addItem("₩500");
        denominationComboBox.addItem("₩1000");

        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        JSpinner quantitySpinner = new JSpinner(spinnerModel);

        Object[] message = {
                "잔돈:", denominationComboBox,
                "개수:", quantitySpinner
        };

        int option = JOptionPane.showOptionDialog(null, message, "잔돈 재고 보충", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (option == JOptionPane.OK_OPTION) {
            String selectedDenomination = (String) denominationComboBox.getSelectedItem();
            int denomination = Integer.parseInt(Objects.requireNonNull(selectedDenomination).substring(1));
            int quantity = (int) quantitySpinner.getValue();

            // 잔돈 재고 보충
            Bill bill = change.findBill(denomination);
            if (bill != null) {
                bill.increaseStore(quantity);
            }

            Coin coin = change.findCoin(denomination);
            if (coin != null) {
                coin.increaseStore(quantity);
            }

            JOptionPane.showMessageDialog(null, "잔돈 보충 성공", "잔돈 재고 보충", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * 음료별 매출을 계산하는 메서드
     */
    public void calculateSalesByBeverage() {
        JComboBox<String> beverageComboBox = new JComboBox<>();
        for (Beverage beverage : beverages) {
            beverageComboBox.addItem(beverage.getName());
        }

        Object[] message = {
                "음료를 선택하시오:", beverageComboBox
        };

        int option = JOptionPane.showOptionDialog(null, message, "음료 선택", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (option == JOptionPane.OK_OPTION) {
            String selectedBeverage = (String) beverageComboBox.getSelectedItem();
            loadSalesData();
            printSalesByBeverage(selectedBeverage);
        }
    }

    /**
     * 매출 데이터를 로드하는 메서드
     */
    private void loadSalesData() {
        try {
            String filePath = "sales.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] saleData = line.split(",");
                if (saleData.length == 2) {
                    String date = saleData[0].trim();
                    String beverageName = saleData[1].trim();

                    int price = findBeveragePrice(beverages, beverageName);
                    if (price != -1) {
                        // 월별 매출 계산
                        String[] dateParts = date.split("-");
                        if (dateParts.length == 3) {
                            String month = dateParts[0] + "-" + dateParts[1];
                            int totalSales = salesByMonth.getOrDefault(month, 0);
                            totalSales += price;
                            salesByMonth.put(month, totalSales);
                        }

                        // 일별 매출 계산
                        int totalSalesByDate = salesByDate.getOrDefault(date, 0);
                        totalSalesByDate += price;
                        salesByDate.put(date, totalSalesByDate);

                        // 음료별 매출 계산
                        Map<String, Integer> sales = salesByBeverage.getOrDefault(beverageName, new TreeMap<>());
                        int totalSales = sales.getOrDefault(date, 0);
                        totalSales += price;
                        sales.put(date, totalSales);
                        salesByBeverage.put(beverageName, sales);
                    } else {
                        System.out.println("음료를 찾을 수 없습니다: " + beverageName);
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("매출 데이터를 읽어오는 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    /**
     * 음료 가격을 찾는 메서드
     */
    private int findBeveragePrice(ArrayList<Beverage> beverages, String name) {
        for (Beverage beverage : beverages) {
            if (beverage.getName().equals(name)) {
                return beverage.getPrice();
            }
        }
        return -1;
    }

    /**
     * 월/일 매출을 계산하는 메서드
     */
    public void calculateMonthlySales() {
        try {
            String filePath = "sales.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            salesByMonth.clear(); // 이전 매출 데이터 초기화

            while ((line = reader.readLine()) != null) {
                String[] saleData = line.split(",");
                if (saleData.length == 2) {
                    String date = saleData[0].trim();
                    String beverageName = saleData[1].trim();

                    int price = findBeveragePrice(beverages, beverageName);
                    if (price != -1) {
                        // 날짜에서 월 정보 추출
                        String[] dateParts = date.split("-");
                        if (dateParts.length == 3) {
                            String month = dateParts[0] + "-" + dateParts[1];

                            int totalSales = salesByMonth.getOrDefault(month, 0);
                            totalSales += price;
                            salesByMonth.put(month, totalSales);
                        }

                        // 날짜별 매출 정보 추가
                        int totalSalesByDate = salesByDate.getOrDefault(date, 0);
                        totalSalesByDate += price;
                        salesByDate.put(date, totalSalesByDate);
                    } else {
                        System.out.println("음료를 찾을 수 없습니다: " + beverageName);
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("매출 데이터를 읽어오는 중 오류가 발생했습니다.");
            e.printStackTrace();
        }

        // 월별 매출 출력
        printMonthlySales();

        // 일별 매출 출력
        printDailySales();
    }

    /**
     * 월별 매출을 출력하는 메서드
     */
    private void printMonthlySales() {
        StringBuilder salesInfo = new StringBuilder();
        salesInfo.append("===== 월별 매출 =====\n");
        for (String month : salesByMonth.keySet()) {
            if (month.matches("\\d{4}-\\d{2}")) { // YYYY-MM 형식만 출력
                int totalSales = salesByMonth.get(month);
                salesInfo.append(month).append(": ₩").append(totalSales).append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, salesInfo.toString(), "월 매출", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 일별 매출을 출력하는 메서드
     */
    private void printDailySales() {
        StringBuilder salesInfo = new StringBuilder();
        salesInfo.append("===== 일별 매출 =====\n");

        Map<String, Integer> dailySales = new HashMap<>(salesByDate); // 복사본 생성
        salesByDate.clear(); // 기존 매출 데이터 초기화

        for (String date : dailySales.keySet()) {
            if (date.matches("\\d{4}-\\d{2}-\\d{2}")) { // YYYY-MM-DD 형식만 출력
                int totalSales = dailySales.get(date);
                salesInfo.append(date).append(": ₩").append(totalSales).append("\n");
            }
        }

        JOptionPane.showMessageDialog(null, salesInfo.toString(), "일 매출", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 음료별 매출을 출력하는 메서드
     */
    private void printSalesByBeverage(String selectedBeverage) {
        StringBuilder salesInfo = new StringBuilder();
        salesInfo.append("===== ").append(selectedBeverage).append(" 매출 =====\n");

        salesInfo.append("월별 매출:\n");
        Map<String, Integer> monthlySales = new TreeMap<>();
        if (salesByBeverage.containsKey(selectedBeverage)) {
            Map<String, Integer> sales = salesByBeverage.get(selectedBeverage);
            for (String date : sales.keySet()) {
                if (date.matches("\\d{4}-\\d{2}-\\d{2}")) { // YYYY-MM-DD 형식만 처리
                    String month = date.substring(0, 7); // YYYY-MM-DD 형식에서 월 부분 추출
                    int totalSales = monthlySales.getOrDefault(month, 0);
                    totalSales += sales.get(date);
                    monthlySales.put(month, totalSales);
                }
            }
        }
        for (String month : monthlySales.keySet()) {
            int totalSales = monthlySales.get(month);
            salesInfo.append(month).append(": ₩").append(totalSales).append("\n");
        }

        salesInfo.append("\n일별 매출:\n");
        if (salesByBeverage.containsKey(selectedBeverage)) {
            Map<String, Integer> sales = salesByBeverage.get(selectedBeverage);
            for (String date : sales.keySet()) {
                if (date.matches("\\d{4}-\\d{2}-\\d{2}")) { // YYYY-MM-DD 형식만 처리
                    int totalSales = sales.get(date);
                    salesInfo.append(date).append(": ₩").append(totalSales).append("\n");
                }
            }
        } else {
            salesInfo.append("선택한 음료의 매출 데이터가 없습니다.\n");
        }

        JOptionPane.showMessageDialog(null, salesInfo.toString(), "음료별 매출", JOptionPane.INFORMATION_MESSAGE);
    }
}
