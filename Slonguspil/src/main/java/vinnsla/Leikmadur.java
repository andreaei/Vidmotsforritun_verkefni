package vinnsla;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Leikmadur {
    private final SimpleStringProperty nafn;
    private final SimpleIntegerProperty oldReitur;
    private final SimpleIntegerProperty nyrReitur;
    private final SimpleIntegerProperty lendingSlangaStigi;
    private final SimpleIntegerProperty sigrar;

    public Leikmadur(String nafn) {
        this.nafn = new SimpleStringProperty(nafn);
        this.oldReitur = new SimpleIntegerProperty(1);
        this.nyrReitur = new SimpleIntegerProperty(1); // Byrjar á reit 1
        this.sigrar = new SimpleIntegerProperty(0);
        this.lendingSlangaStigi = new SimpleIntegerProperty();
    }

    public String getNafn() {
        return nafn.get();
    }

    public SimpleStringProperty nafnProperty() {
        return nafn;
    }

    public int getOldReitur() {
        return oldReitur.get();
    }

    public void setOldReitur(int reitur) {
        this.oldReitur.set(reitur);
    }

    public int getNyrReitur() {
        return nyrReitur.get();
    }

    public void setNyrReitur(int reitur) {
        this.nyrReitur.set(reitur);
    }

    public void sigur(){
        this.sigrar.set(getSigrar()+1);
    }

    public int getSigrar(){
        return sigrar.get();
    }

    public int getLendingSlonguStiga(){
        return lendingSlangaStigi.get();
    }

    public void setLendingSlonguStiga(int lending){
        this.lendingSlangaStigi.set(lending);
    }

    public SimpleIntegerProperty sigrarProperty() {
        return sigrar;
    }

    public SimpleIntegerProperty oldReiturProperty() {
        return oldReitur;
    }

    public SimpleIntegerProperty nyrReiturProperty() {
        return nyrReitur;
    }



    /**
     * Færir leikmann á reit reitur en markið er í reit max
     * @param reitur reitur sem leikmaður er færður á
     * @param max mark-reitur á borðinu
     */
    public void faera (int reitur, int max) {
        if(reitur >= max){
            setOldReitur(getNyrReitur());
            setNyrReitur(max);
        }
        else {
            setOldReitur(getNyrReitur());
            setNyrReitur(reitur);
        }

    }

    public static void main(String[] args) {

    }
}
