import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VendingMachineGUI {
    public static void main(String[] args) {
        // VendingMachine 인스턴스 생성
        Beverage[] beverages = {}; // 음료 배열 초기화
        Coin[] coins = {}; // 화폐 배열 초기화
        String salesFilePath = "sales.txt"; // 매출 파일 경로 초기화
        String stockFilePath = "stock.txt"; // 재고 파일 경로 초기화

        VendingMachine vendingMachine = new VendingMachine(beverages, coins, salesFilePath, stockFilePath);

        // JFrame 생성
        JFrame frame = new JFrame("자판기");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JPanel 생성
        JPanel panel = new JPanel();

        // JLabel 생성
        JLabel label = new JLabel("돈을 넣어주세요.");
        panel.add(label);

        // JButton 생성
        JButton sellButton = new JButton("음료 판매");
        sellButton.addActionListener(e -> {
            // 버튼 클릭 이벤트 처리
            // sellBeverage() 메서드 호출 등
        });
        panel.add(sellButton);

        // JButton 생성
        JButton returnChangeButton = new JButton("잔돈 반환");
        returnChangeButton.addActionListener(e -> {
            // 버튼 클릭 이벤트 처리
            // returnChange() 메서드 호출 등
        });
        panel.add(returnChangeButton);

        // JButton 생성
        JButton adminMenuButton = new JButton("관리자 메뉴");
        adminMenuButton.addActionListener(e -> {
            // 버튼 클릭 이벤트 처리
            // accessAdminMenu() 메서드 호출 등
        });
        panel.add(adminMenuButton);

        // JPanel을 JFrame에 추가
        frame.getContentPane().add(panel);

        // JFrame 크기 설정 및 표시
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}
