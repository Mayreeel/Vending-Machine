import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

public class VendingMachine implements IVendingMachine {
    private Beverage[] beverages;
    private Coin[] coins;
    private String salesFilePath;
    private String stockFilePath;

    public VendingMachine(Beverage[] beverages, Coin[] coins, String salesFilePath, String stockFilePath) {
        this.beverages = beverages;
        this.coins = coins;
        this.salesFilePath = salesFilePath;
        this.stockFilePath = stockFilePath;
    }

    @Override
    public void sellBeverage(int index) {
        if (index >= 0 && index < beverages.length) {
            Beverage selectedBeverage = beverages[index];
            try {
                if (selectedBeverage.getStock() > 0) {
                    System.out.println("음료 " + selectedBeverage.getName() + "을/를 선택하셨습니다. 가격: " + selectedBeverage.getPrice() + "원");
                    selectedBeverage.decreaseStock();
                    // 화폐 처리 등 추가 구현 로직
                } else {
                    System.out.println("음료 " + selectedBeverage.getName() + "은/는 품절되었습니다.");
                }
            } catch (SoldOutException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("유효하지 않은 음료 번호입니다.");
        }
    }

    @Override
    public void returnChange(int amount) {
        Change change = new Change();
        try {
            int[] coins = change.calculateChange(amount);
            System.out.println("반환된 화폐: " + Arrays.toString(coins));
        } catch (InsufficientChangeException e) {
            System.out.println("환불할 화폐가 부족합니다.");
        }
    }

    @Override
    public void accessAdminMenu(String password) {
        if (password.equals("admin1234")) {
            Change change = new Change();
            AdminMenu adminMenu = new AdminMenu(change, beverages);
            adminMenu.showMenu();
        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
        }
    }

    private double readSalesFromFile(String filePath, String date, String beverageName) throws IOException {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data.length == 3 && data[0].equals(date) && data[1].equals(beverageName)) {
                    return Double.parseDouble(data[2]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("매출 파일이 없습니다.");
        }

        return 0.0;
    }

    // VendingMachine.java
    @Override
    public void calculateDailySales() {
        LocalDate date = LocalDate.now();
        double totalSales;
        try {
            totalSales = readSalesFromFile(salesFilePath, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), null);
        } catch (IOException e) {
            System.out.println("매출 파일을 읽어오는 중 오류가 발생했습니다.");
            return;
        }

        System.out.println("일별 매출 산출");
        System.out.println(date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")) + " 매출: " + totalSales + "원");
    }

    @Override
    public void calculateMonthlySales() {
        YearMonth yearMonth = YearMonth.now();
        double totalSales = 0.0;
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            try {
                totalSales = readSalesFromFile(salesFilePath, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), null);
            } catch (IOException e) {
                System.out.println("매출 파일을 읽어오는 중 오류가 발생했습니다.");
                return;
            }
        }

        System.out.println("월별 매출 산출");
        System.out.println(yearMonth.format(DateTimeFormatter.ofPattern("yyyy년 MM월")) + " 매출: " + totalSales + "원");
    }


    // Getter, Setter, 그 외 필요한 메서드 구현

    @Override
    public String toString() {
        return "VendingMachine{" +
                "beverages=" + Arrays.toString(beverages) +
                ", coins=" + Arrays.toString(coins) +
                ", salesFilePath='" + salesFilePath + '\'' +
                ", stockFilePath='" + stockFilePath + '\'' +
                '}';
    }
}
