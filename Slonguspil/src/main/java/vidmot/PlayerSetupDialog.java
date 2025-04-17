package vidmot;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlayerSetupDialog {

    private String player1Name;
    private String player2Name;
    //passa að leikur byrji ekki ef dialog er lokað með windows X takkanum
    private boolean confirmed = false;
    public boolean isConfirmed() {
        return confirmed;
    }

    public void showAndWait() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Nýr leikur");

        Label label1 = new Label("Leikmaður 1:");
        TextField name1 = new TextField();

        Label label2 = new Label("Leikmaður 2:");
        TextField name2 = new TextField();


        // Byrja leik takki
        Button startButton = new Button("Byrja leik");
        startButton.setOnAction(e -> {
            player1Name = name1.getText().trim();
            player2Name = name2.getText().trim();

            if (player1Name.isEmpty() || player2Name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Vinsamlegast sláðu inn nöfn beggja leikmanna.");
                alert.showAndWait();
            } else {
                dialog.close();
                confirmed = true;
            }
        });


        Button quitButton = new Button("Hætta");
        quitButton.setOnAction(e -> {
            Platform.exit();
            dialog.close();
        });

        HBox buttonBox = new HBox(10, startButton, quitButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);

        VBox vbox = new VBox(10, label1, name1, label2, name2, buttonBox);
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
