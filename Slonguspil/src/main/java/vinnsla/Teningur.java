package vinnsla;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Random;

public class Teningur {

    private static final int MAX = 6;
    private final SimpleIntegerProperty talaProperty = new SimpleIntegerProperty(MAX);
    final Random rand = new Random();

    public void kasta(){
        talaProperty.set(rand.nextInt(MAX) +1);
    }

    public int getTening(){
        return talaProperty.get();
    }

    public SimpleIntegerProperty teningurProperty(){
        return talaProperty;
    }

    public static void main(String[] args) {

    }
}
