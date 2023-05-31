import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AdminMenu {
    private static final String PASSWORD = "admin1234";  // 비밀번호
    private static final String FILE_PATH = "sales.txt";  // 매출 파일 경로
    private static final String DATE_FORMAT = "yyyy-MM-dd";  // 날짜 형식
    private static final String MONTH_FORMAT = "yyyy-MM";  // 월 형식

    private Change change;
    private Beverage[] beverages;

    public AdminMenu(Change change, Beverage[] beverages) {
        this.change = change;
        this.beverages = beverages;
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("관리자 메뉴");
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();

        if (password.equals(PASSWORD)) {
            while (true) {
                System.out.println();
                System.out.println("1. 일별 매출 산출");
                System.out.println("2. 월별 매출 산출");
                System.out.println("3. 각 음료 일별 매출 산출");
                System.out.println("4. 각 음료 월별 매출 산출");
                System.out.println("5. 각 음료 재고 보충");
                System.out.println("6. 화폐 현황 파악");
                System.out.println("7. 수금");
                System.out.println("8. 음료 가격 및 이름 변경");
                System.out.println("9. 종료");
                System.out.print("메뉴를 선택하세요: ");
                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        calculateDailySales();
                        break;
                    case 2:
                        calculateMonthlySales();
                        break;
                    case 3:
                        calculateDailySalesByBeverage();
                        break;
                    case 4:
                        calculateMonthlySalesByBeverage();
                        break;
                    case 5:
                        replenishStock();
                        break;
                    case 6:
                        checkChangeStatus();
                        break;
                    case 7:
                        collectMoney();
                        break;
                    case 8:
                        changeBeverageInfo();
                        break;
                    case 9:
                        return;
                    default:
                        System.out.println("유효하지 않은 메뉴 번호입니다.");
                        break;
                }
            }
        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
        }
    }

    private void calculateDailySales() {
        LocalDate date = LocalDate.now();
        double totalSales;
        try {
            totalSales = readSalesFromFile(date.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        } catch (IOException e) {
            System.out.println("매출 파일을 읽어오는 중 오류가 발생했습니다.");
            return;
        }

        System.out.println("일별 매출 산출");
        System.out.println(date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")) + " 매출: " + totalSales + "원");
    }

    private void calculateMonthlySales() {
        YearMonth yearMonth = YearMonth.now();
        double totalSales = 0.0;
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            try {
                totalSales += readSalesFromFile(date.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
            } catch (IOException e) {
                System.out.println("매출 파일을 읽어오는 중 오류가 발생했습니다.");
                return;
            }
        }

        System.out.println("월별 매출 산출");
        System.out.println(yearMonth.format(DateTimeFormatter.ofPattern("yyyy년 MM월")) + " 매출: " + totalSales + "원");
    }

    private void calculateDailySalesByBeverage() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("각 음료 일별 매출 산출");
        System.out.print("음료 번호를 입력하세요: ");
        int beverageNumber;
        try {
            beverageNumber = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
            return;
        }

        if (beverageNumber < 1 || beverageNumber > beverages.length) {
            System.out.println("유효하지 않은 음료 번호입니다.");
            return;
        }

        Beverage beverage = beverages[beverageNumber - 1];
        LocalDate date = LocalDate.now();
        double sales;
        try {
            sales = readSalesFromFile(date.format(DateTimeFormatter.ofPattern(DATE_FORMAT)), beverage.getName());
        } catch (IOException e) {
            System.out.println("매출 파일을 읽어오는 중 오류가 발생했습니다.");
            return;
        }

        System.out.println(date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")) + " " + beverage.getName() + " 매출: " + sales + "원");
    }

    private void calculateMonthlySalesByBeverage() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("각 음료 월별 매출 산출");
        System.out.print("음료 번호를 입력하세요: ");
        int beverageNumber;
        try {
            beverageNumber = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
            return;
        }

        if (beverageNumber < 1 || beverageNumber > beverages.length) {
            System.out.println("유효하지 않은 음료 번호입니다.");
            return;
        }

        Beverage beverage = beverages[beverageNumber - 1];
        YearMonth yearMonth = YearMonth.now();
        double totalSales = 0.0;
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            try {
                totalSales += readSalesFromFile(date.format(DateTimeFormatter.ofPattern(DATE_FORMAT)), beverage.getName());
            } catch (IOException e) {
                System.out.println("매출 파일을 읽어오는 중 오류가 발생했습니다.");
                return;
            }
        }

        System.out.println(yearMonth.format(DateTimeFormatter.ofPattern("yyyy년 MM월")) + " " + beverage.getName() + " 매출: " + totalSales + "원");
    }

    private void replenishStock() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("각 음료 재고 보충");
        System.out.print("음료 번호를 입력하세요: ");
        int beverageNumber;
        try {
            beverageNumber = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
            return;
        }

        if (beverageNumber < 1 || beverageNumber > beverages.length) {
            System.out.println("유효하지 않은 음료 번호입니다.");
            return;
        }

        Beverage beverage = beverages[beverageNumber - 1];

        System.out.print("보충할 음료 개수를 입력하세요: ");
        int amount;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
            return;
        }

        beverage.replenishStock(amount);

        System.out.println(beverage.getName() + " 재고가 보충되었습니다.");
    }

    private void checkChangeStatus() {
        Coin[] coins = change.getCoins();

        System.out.println("화폐 현황 파악");
        for (Coin coin : coins) {
            System.out.println(coin.getDenomination() + "원 동전 재고: " + coin.getStock() + "개");
        }
    }

    private void collectMoney() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("수금");
        System.out.print("수금할 금액을 입력하세요: ");
        int amount;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
            return;
        }

        // 최소한의 화폐 보존 (임의로 지정)
        int[] minimumCoins = {0, 1, 1, 0, 0};  // 100원, 500원 동전은 보존

        int[] collectedCoins = change.calculateChange(amount);
        if (collectedCoins != null) {
            for (int i = 0; i < collectedCoins.length; i++) {
                if (collectedCoins[i] > minimumCoins[i]) {
                    change.getCoins()[i].increaseStock(collectedCoins[i] - minimumCoins[i]);
                }
            }

            System.out.println(amount + "원이 수금되었습니다.");
        } else {
            System.out.println("수금할 수 없는 금액입니다.");
        }
    }

    private void changeBeverageInfo() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("음료 가격 및 이름 변경");
        System.out.print("음료 번호를 입력하세요: ");
        int beverageNumber;
        try {
            beverageNumber = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
            return;
        }

        if (beverageNumber < 1 || beverageNumber > beverages.length) {
            System.out.println("유효하지 않은 음료 번호입니다.");
            return;
        }

        Beverage beverage = beverages[beverageNumber - 1];

        System.out.print("새로운 음료 가격을 입력하세요: ");
        int newPrice;
        try {
            newPrice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
            return;
        }

        System.out.print("새로운 음료 이름을 입력하세요: ");
        String newName = scanner.nextLine();

        beverage.setPrice(newPrice);
        beverage.setName(newName);

        System.out.println("음료 정보가 변경되었습니다.");
    }

    private double readSalesFromFile(String date) throws IOException {
        // 매출 파일에서 해당 날짜의 매출을 읽어오는 로직을 구현해야 합니다.
        // 예시로 파일이 없거나 읽어올 수 없는 경우에는 0.0을 반환합니다.
        return 0.0;
    }

    private double readSalesFromFile(String date, String beverageName) throws IOException {
        // 매출 파일에서 해당 날짜와 음료 이름의 매출을 읽어오는 로직을 구현해야 합니다.
        // 예시로 파일이 없거나 읽어올 수 없는 경우에는 0.0을 반환합니다.
        return 0.0;
    }
}