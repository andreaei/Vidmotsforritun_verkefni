package vinnsla;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Leikur {

    private Leikmadur leikmadur1;
    private Leikmadur leikmadur2;
    private final SlongurStigar slongurStigar = new SlongurStigar();
    private final Teningur teningur = new Teningur();
    final int fjoldiReita = 24;
    private final SimpleBooleanProperty LeikLokid;
    private final SimpleStringProperty Sigurvegari;
    private final SimpleStringProperty HverALeik;

    Leikmadur leikmadurCurrent;
    Leikmadur leikmadurNext;



    public Leikur(){
        this.LeikLokid = new SimpleBooleanProperty(true);
        this.Sigurvegari = new SimpleStringProperty("");
        this.HverALeik = new SimpleStringProperty("");
    }

    public void setLeikmenn(String nafn1, String nafn2){
        leikmadur1 = new Leikmadur(nafn1);
        leikmadur2 = new Leikmadur(nafn2);

    }

    public void randomStart(){
        int randomStart = 1 + (int)(Math.random() * ((2 - 1) + 1));
        String fyrstiLeikmadur = ((randomStart==1) ? leikmadur1.getNafn() : leikmadur2.getNafn());
        HverALeik.set(fyrstiLeikmadur);
    }

    /**
     * kastar tening, færir leikmann, setur næsta leikmann
     *
     * @return skilar true ef leik er lokið
     */
    public boolean leikaLeik(){
        teningur.kasta();

        //Hver á að gera
        if(getHverALeik().equals(leikmadur1.getNafn())){
            leikmadurCurrent = leikmadur1;
            leikmadurNext = leikmadur2;
        }
        else {
            leikmadurCurrent = leikmadur2;
            leikmadurNext = leikmadur1;
        }

        //færa leikmanninn, athuga sigur og skipta í næsta leikmann
        int currentReitur = leikmadurCurrent.getNyrReitur();
        int nyrReitur = currentReitur+teningur.getTening();

        leikmadurCurrent.setLendingSlonguStiga(slongurStigar.LendingarReitur(nyrReitur, leikmadurCurrent.getNafn()));
        leikmadurCurrent.faera(nyrReitur,fjoldiReita);
        //Sigur
        if(leikmadurCurrent.getNyrReitur() == 24){
            Sigurvegari.set(getHverALeik());
            System.out.println("Win");
            leikmadurCurrent.sigur();
            System.out.println(leikmadur1.getSigrar());
            System.out.println(leikmadur2.getSigrar());
            LeikLokid.set(true);
            return true;
        }
        HverALeik.set(leikmadurNext.getNafn());

        return false;
    }

    /**
     * Hefur nýjan leik. Leikmenn settir á reit eitt
     */

    public void nyrLeikur(){
        LeikLokid.set(false);
        Sigurvegari.set("");
        leikmadur1.setNyrReitur(1);
        leikmadur2.setNyrReitur(1);
        leikmadur1.setOldReitur(1);
        leikmadur2.setOldReitur(1);
        randomStart();
        slongurStigar.FaersluSkilabodProperty().set("");
    }

    public String getHverALeik(){
        return HverALeik.get();
    }

    public String getSigurvegari(){
        return Sigurvegari.get();
    }

    public SlongurStigar getSlongurStigar(){
        return slongurStigar;
    }

    public Leikmadur getLeikmadur1(){
        return leikmadur1;
    }

    public Leikmadur getLeikmadur2(){
        return leikmadur2;
    }

    public Leikmadur getLeikmadurCurrent(){
        return  leikmadurCurrent;
    }

    public SimpleStringProperty hverALeikProperty(){
        return HverALeik;
    }

    public SimpleStringProperty sigurvegariProperty(){
        return Sigurvegari;
    }

    public SimpleBooleanProperty LeikLokidProperty(){
        return LeikLokid;
    }

    public SimpleStringProperty faersluSkilabodProperty(){
        return slongurStigar.FaersluSkilabodProperty();
    }

    public SimpleIntegerProperty teningurProperty(){
        return teningur.teningurProperty();
    }


    public static void main(String[] args) {

    }
}
