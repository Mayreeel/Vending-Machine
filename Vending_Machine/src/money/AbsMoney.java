package money;

public abstract class AbsMoney {
    int unit; // 화폐 단위
    int store; // 거스름돈 갯수

    protected AbsMoney(int unit, int store) {
        this.unit = unit;
        this.store = store;
    }

    int getUnit(){
        return unit;
    }

    int getStore(){
        return store;
    }
}
