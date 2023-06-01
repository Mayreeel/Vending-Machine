import java.util.Arrays;

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
        // 화폐 반환 처리 등 구현 로직
    }

    @Override
    public void accessAdminMenu(String password) {
        // 관리자 메뉴 접근 처리 등 구현 로직
    }

    @Override
    public void calculateDailySales() {
        // 일별 매출 산출 등 구현 로직
    }

    @Override
    public void calculateMonthlySales() {
        // 월별 매출 산출 등 구현 로직
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