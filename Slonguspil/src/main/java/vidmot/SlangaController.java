package vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import vinnsla.Leikur;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SlangaController {

    @FXML
    private Button fxTeningurButton;
    private ImageView teningurImageView;

    @FXML
    private Button fxPlayButton;

    @FXML
    private  Button fxNyrLeikurButton;

    @FXML
    private Button fxQuitButton;

    @FXML
    private Label fxSkilabod1;

    @FXML
    private Label fxSkilabod2;

    @FXML
    private Label fxPlayer1WinCountLabel;

    @FXML
    private Label fxPlayer2WinCountLabel;

    @FXML
    private GridPane fxBord;

    @FXML
    private Pane playerPane;


    private List<Node> reitir = new ArrayList<>();

    private Leikur leikur;
    private ImageView leikmadur1Icon;
    private ImageView leikmadur2Icon;
    private ImageView slangaIcon;
    private ImageView stigiIcon;

    public void initialize(){

        leikur = new Leikur();
        leikur.LeikLokidProperty().set(true);

        reitir.clear();
        fxBord.getChildren().clear();

        System.out.println("Texti á fxPlayButton er: " + fxPlayButton.getText());


        //Opna Dialog í byrjun leiks. setja inn nöfn playera
        PlayerSetupDialog dialog = new PlayerSetupDialog();
        dialog.showAndWait();
        if (!dialog.isConfirmed()) {
            System.exit(0);
        }
        playerHandler(dialog.getPlayer1Name(),dialog.getPlayer2Name());

        int rows = 4;
        int cols = 6;
        int tileNumber = 1;

        for (int row = rows - 1; row >= 0; row--) { // fá sick sack fyrir röðun reita
            if ((rows - 1 - row) % 2 == 0) {
                for (int col = 0; col < cols; col++) {
                    addNumberedTile(col, row, tileNumber++);
                }
            } else {
                for (int col = cols - 1; col >= 0; col--) {
                    addNumberedTile(col, row, tileNumber++);
                }
            }
        }


        leikmadur1Icon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/vidmot/css/myndir/Player1.png"))));
        leikmadur2Icon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/vidmot/css/myndir/Player2.png"))));

        leikmadur1Icon.setFitWidth(40);
        leikmadur1Icon.setFitHeight(40);
        leikmadur2Icon.setFitWidth(40);
        leikmadur2Icon.setFitHeight(40);


        // Bæta við leikmönnum í playerPane þegar leikur byrjar
        playerPane.getChildren().add(leikmadur1Icon);
        playerPane.getChildren().add(leikmadur2Icon);

        Platform.runLater(()-> {
            movePlayerSmoothly(leikmadur1Icon, 1);
            movePlayerSmoothly(leikmadur2Icon, 1);
        });




        slangaIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/vidmot/css/myndir/snake.png"))));
        stigiIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/vidmot/css/myndir/ladder.png"))));

        slangaIcon.setFitWidth(40);
        slangaIcon.setFitHeight(40);
        stigiIcon.setFitWidth(40);
        stigiIcon.setFitHeight(40);

        placeSlongurStigar("/vidmot/css/myndir/snake.png", 8);
        placeSlongurStigar("/vidmot/css/myndir/snake.png", 16);
        placeSlongurStigar("/vidmot/css/myndir/snake.png", 23);
        placeSlongurStigar("/vidmot/css/myndir/ladder.png", 3);
        placeSlongurStigar("/vidmot/css/myndir/ladder.png", 7);
        placeSlongurStigar("/vidmot/css/myndir/ladder.png", 15);



        teningurImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/vidmot/css/myndir/dice1.png"))));
        teningurImageView.setFitWidth(200);
        teningurImageView.setFitHeight(200);
        teningurImageView.setPreserveRatio(true);
        fxTeningurButton.setGraphic(teningurImageView);


        //Disable Button þegar þeir eiga ekki að vera notaðir
        fxPlayButton.disableProperty().bind(leikur.LeikLokidProperty().not());
        fxTeningurButton.setDisable(false);

        //Tengja skilaboð labels við viðeigandi breytur
        fxSkilabod2.textProperty().bind(leikur.getSlongurStigar().FaersluSkilabodProperty());
        fxSkilabod1.textProperty().bind(
                Bindings.when(leikur.LeikLokidProperty())
                        .then(Bindings.concat("Sigurvegari: ", leikur.sigurvegariProperty()))
                        .otherwise(Bindings.concat("Á að gera: ", leikur.hverALeikProperty()))
        );



        teningurImageView.setFitWidth(200);
        teningurImageView.setFitHeight(200);
        teningurImageView.setPreserveRatio(true);

    }

    public void playerHandler(String nafn1, String nafn2){
        leikur.setLeikmenn(nafn1,nafn2);
        leikur.nyrLeikur(); //byrjum leikinn

        fxPlayer1WinCountLabel.textProperty().unbind();
        fxPlayer1WinCountLabel.textProperty().bind(
                Bindings.concat(
                        leikur.getLeikmadur1().nafnProperty(),
                        " sigrar: ",
                        leikur.getLeikmadur1().sigrarProperty()
                )
        );

        fxPlayer2WinCountLabel.textProperty().unbind();
        fxPlayer2WinCountLabel.textProperty().bind(
                Bindings.concat(
                        leikur.getLeikmadur2().nafnProperty(),
                        " sigrar: ",
                        leikur.getLeikmadur2().sigrarProperty()
                )
        );

    }

    public void teningurHandler(ActionEvent actionEvent) {
        System.out.println("teningur");
        fxTeningurButton.setDisable(true); // Ekki hægt að smella á meðan "rúllar"

        Timeline timeline = new Timeline();
        Random rand = new Random();
        int fjoldiRulla = 5;
        int timiMilliRulla = 100; //milisek

        for (int i = 0; i < fjoldiRulla; i++) {
            int delay = i * timiMilliRulla;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(delay), e -> {
                int fakeRoll = rand.nextInt(6) + 1;
                String fakeImagePath = "/vidmot/css/myndir/dice" + fakeRoll + ".png";
                teningurImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(fakeImagePath))));
            }));
        }

        timeline.setOnFinished(e -> {
            // Alvöru kast
            boolean gameOver = leikur.leikaLeik();

            // Uppfæra með rétta teningatöluna
            int realRoll = leikur.teningurProperty().get();
            String realImagePath = "/vidmot/css/myndir/dice" + realRoll + ".png";
            teningurImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(realImagePath))));

            // Hreyfa leikmenn
            movePlayerSmoothly(leikmadur1Icon, leikur.getLeikmadur1().getReitur());
            movePlayerSmoothly(leikmadur2Icon, leikur.getLeikmadur2().getReitur());

            // Ef leik lokið
            if (gameOver) {
                leikur.LeikLokidProperty().set(true);
            }
            if(!leikur.LeikLokidProperty().get()){
                fxTeningurButton.setDisable(false);
            }

        });

        timeline.play();
    }


    public void playAgainHandler(ActionEvent actionEvent){
        leikur.nyrLeikur();
        fxBord.getChildren().remove(leikmadur1Icon);
        fxBord.getChildren().remove(leikmadur2Icon);

        movePlayerSmoothly(leikmadur1Icon, 1);
        movePlayerSmoothly(leikmadur2Icon, 1);

        fxTeningurButton.setDisable(false);

    }

    public void nyrLeikurHandler(ActionEvent actionEvent){
        PlayerSetupDialog dialog = new PlayerSetupDialog();
        dialog.showAndWait();
        if (!dialog.isConfirmed()) {
            System.exit(0);
        }

        playerHandler(dialog.getPlayer1Name(), dialog.getPlayer2Name());
        leikur.nyrLeikur();

        movePlayerSmoothly(leikmadur1Icon, 1);
        movePlayerSmoothly(leikmadur2Icon, 1);

        fxTeningurButton.setDisable(false);

    }

    public void quitHandler(ActionEvent actionEvent){
        Platform.exit();
    }

    private void addNumberedTile(int col, int row, int number) {
        //passa upp á fuplicate tiles
        for (Node node : fxBord.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            Integer nodeRow = GridPane.getRowIndex(node);

            if (nodeCol != null && nodeRow != null && nodeCol == col && nodeRow == row) {
                return;
            }
        }


        Label tile = new Label(String.valueOf(number));
        tile.getStyleClass().add("cell");
        tile.setMinSize(50, 50);
        tile.setAlignment(Pos.CENTER);

        fxBord.add(tile, col, row);
        reitir.add(tile);
    }


    private void placeSlongurStigar(String imagePath, int tileNumber) {
        ImageView icon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
        icon.setFitWidth(40);
        icon.setFitHeight(40);

        for (Node node : reitir) {
            if (node instanceof Label && ((Label) node).getText().equals(String.valueOf(tileNumber))) {
                GridPane.setColumnIndex(icon, GridPane.getColumnIndex(node));
                GridPane.setRowIndex(icon, GridPane.getRowIndex(node));
                fxBord.getChildren().add(icon);
                return;
            }
        }
    }

    private void movePlayerSmoothly(ImageView playerIcon, int reitur) {
        for (Node node : reitir) {
            if (node instanceof Label && ((Label) node).getText().equals(String.valueOf(reitur))) {
                Bounds bounds = node.localToScene(node.getBoundsInLocal());

                double newX = bounds.getMinX();
                double newY = bounds.getMinY();

                // Breyta í staðsetningu innan fxBord (Pane)
                Point2D parentCoords = fxBord.sceneToLocal(newX, newY);

                TranslateTransition transition = new TranslateTransition(Duration.millis(200), playerIcon);
                transition.setToX(parentCoords.getX());
                transition.setToY(parentCoords.getY());
                transition.play();

                return;
            }
        }
    }


}
