package vinnsla;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Leikmadur {
    private final SimpleStringProperty nafn;
    private final SimpleIntegerProperty reitur;
    private final SimpleIntegerProperty sigrar;

    public Leikmadur(String nafn) {
        this.nafn = new SimpleStringProperty(nafn);
        this.reitur = new SimpleIntegerProperty(0); // Byrjar á reit 0
        this.sigrar = new SimpleIntegerProperty(0);
    }

    public String getNafn() {
        return nafn.get();
    }

    public SimpleStringProperty nafnProperty() {
        return nafn;
    }

    public int getReitur() {
        return reitur.get();
    }

    public void setReitur(int reitur) {
        this.reitur.set(reitur);
    }

    public void sigur(){
        this.sigrar.set(getSigrar()+1);
    }

    public int getSigrar(){
        return sigrar.get();
    }

    public SimpleIntegerProperty sigrarProperty() {
        return sigrar;
    }

    public SimpleIntegerProperty reiturProperty() {
        return reitur;
    }

    /**
     * Færir leikmann á reit reitur en markið er í reit max
     * @param reitur reitur sem leikmaður er færður á
     * @param max mark-reitur á borðinu
     */
    public void faera (int reitur, int max) {
        if(reitur >= max){
            setReitur(max);
        }
        else {
            setReitur(reitur);
        }

    }

    public static void main(String[] args) {

    }
}
