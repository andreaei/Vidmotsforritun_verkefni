package vinnsla;

import javafx.beans.property.SimpleStringProperty;

import java.util.HashMap;

public class SlongurStigar {

    private HashMap<Integer, Integer> slongurStigar;
    private final SimpleStringProperty faersluSkilabod ; // Skilaboð um hvert og hvaðan leikmaður fór

    public SlongurStigar(){
        slongurStigar = new HashMap<>();
        faersluSkilabod = new SimpleStringProperty();

        slongurStigar.put(3, 10); //Stigi
        slongurStigar.put(7, 18); //Stigi
        slongurStigar.put(15, 22); //Stigi

        slongurStigar.put(8, 5); //Slanga
        slongurStigar.put(16,9); //Slanga
        slongurStigar.put(23, 14); //Slanga

    }

    public int LendingarReitur(int reitur, String leikmadur){
        int nyrReitur = slongurStigar.getOrDefault(reitur,reitur);
        if(reitur>nyrReitur){
            faersluSkilabod.set(leikmadur + " þú fórst niður slöngu!!! Þú fórst frá " + reitur + " til " + nyrReitur);
        } else if (reitur<nyrReitur) {
            faersluSkilabod.set(leikmadur + " þú fórst upp stiga!!! Þú fórst frá " + reitur + " til" + nyrReitur);
        }
        else { // þú lentir ekki á stiga né slöngu
            faersluSkilabod.set(leikmadur + " þú fórst áfram á reit "+nyrReitur);
        }
        return nyrReitur;
    }

    public SimpleStringProperty FaersluSkilabodProperty(){
        return faersluSkilabod;
    }

    public static void main(String[] args) {

    }
}
