package vidmot;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlayerSetupDialog {

    private String player1Name;
    private String player2Name;

    public void showAndWait() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Nýr leikur");

        Label label1 = new Label("Leikmaður 1:");
        TextField name1 = new TextField();

        Label label2 = new Label("Leikmaður 2:");
        TextField name2 = new TextField();

        Button startButton = new Button("Byrja leik");
        startButton.setOnAction(e -> {
            player1Name = name1.getText().trim();
            player2Name = name2.getText().trim();

            if (player1Name.isEmpty() || player2Name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Vinsamlegast sláðu inn nöfn beggja leikmanna.");
                alert.showAndWait();
            } else {
                dialog.close();
            }
        });

        VBox vbox = new VBox(10, label1, name1, label2, name2, startButton);
        vbox.setPadding(new Insets(20));
        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }
}
