public interface IVendingMachine {
    void sellBeverage(int index);
    void returnChange(int amount);
    void accessAdminMenu(String password);
    void calculateDailySales();
    void calculateMonthlySales();
}