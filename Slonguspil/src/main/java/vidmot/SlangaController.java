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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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
            setPlayerPosition(leikmadur1Icon, 1);
            setPlayerPosition(leikmadur2Icon, 1);

            teiknaSlonguStiga(8,5,1);
            teiknaSlonguStiga(16,9,1);
            teiknaSlonguStiga(23,14,1);

            teiknaSlonguStiga(3,10,2);
            teiknaSlonguStiga(7,18,2);
            teiknaSlonguStiga(15,22,2);
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
                        .otherwise(Bindings.concat(leikur.hverALeikProperty()," á að gera"))
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
            if(leikur.getLeikmadurCurrent().equals(leikur.getLeikmadur1())){
                movePlayer(leikmadur1Icon, leikur.getLeikmadur1().getOldReitur(), leikur.getLeikmadur1().getNyrReitur());
            }

            else{
                movePlayer(leikmadur2Icon, leikur.getLeikmadur2().getOldReitur(), leikur.getLeikmadur2().getNyrReitur());
            }

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

        setPlayerPosition(leikmadur1Icon, 1);
        setPlayerPosition(leikmadur2Icon, 1);

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

        setPlayerPosition(leikmadur1Icon, 1);
        setPlayerPosition(leikmadur2Icon, 1);

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

    private void movePlayer(ImageView playerIcon, int fromTile, int toTile) {
        List<Integer> path = new ArrayList<>();
        for (int i = fromTile + 1; i <= toTile; i++) {
            path.add(i);
        }

        moveStepByStep(playerIcon, path, 0);
    }

    private void moveStepByStep(ImageView playerIcon, List<Integer> path, int index) {
        if (index >= path.size()) {
            // Step movement finished, now check for snake/ladder
            int finalTile = path.get(path.size() - 1);
            int lendingSlonguStiga = leikur.getLeikmadurCurrent().getLendingSlonguStiga();
            if (lendingSlonguStiga != finalTile) {
                moveDirect(playerIcon, lendingSlonguStiga);
            } else {
                fxTeningurButton.setDisable(false); // Leyfa næsta kast ef engin slanga/stigi
            }
            return;
        }

        int reitur = path.get(index);
        for (Node node : reitir) {
            if (node instanceof Label && ((Label) node).getText().equals(String.valueOf(reitur))) {
                Bounds bounds = node.localToScene(node.getBoundsInLocal());
                Point2D parentCoords = fxBord.sceneToLocal(bounds.getMinX(), bounds.getMinY());

                TranslateTransition transition = new TranslateTransition(Duration.millis(200), playerIcon);
                transition.setToX(parentCoords.getX());
                transition.setToY(parentCoords.getY());
                transition.setOnFinished(e -> moveStepByStep(playerIcon, path, index + 1));
                transition.play();
                break;
            }
        }
    }

    private void setPlayerPosition(ImageView playerIcon, int tile) {
        for (Node node : reitir) {
            if (node instanceof Label && ((Label) node).getText().equals(String.valueOf(tile))) {
                Bounds bounds = node.localToScene(node.getBoundsInLocal());
                Point2D parentCoords = fxBord.sceneToLocal(bounds.getMinX(), bounds.getMinY());

                playerIcon.setTranslateX(parentCoords.getX());
                playerIcon.setTranslateY(parentCoords.getY());
                break;
            }
        }
    }

    private void moveDirect(ImageView playerIcon, int reitur) {
        for (Node node : reitir) {
            if (node instanceof Label && ((Label) node).getText().equals(String.valueOf(reitur))) {
                Bounds bounds = node.localToScene(node.getBoundsInLocal());
                Point2D parentCoords = fxBord.sceneToLocal(bounds.getMinX(), bounds.getMinY());

                TranslateTransition transition = new TranslateTransition(Duration.millis(300), playerIcon);
                transition.setToX(parentCoords.getX());
                transition.setToY(parentCoords.getY());
                transition.play();
                leikur.getLeikmadurCurrent().faera(reitur,24);
                break;
            }
        }
    }

    private void teiknaSlonguStiga(int fromTile, int toTile,int slangaStigi) {
        Node fromNode = getNode(fromTile);
        Node toNode = getNode(toTile);

        Bounds fromBounds = fromNode.localToScene(fromNode.getBoundsInLocal());
        Bounds toBounds = toNode.localToScene(toNode.getBoundsInLocal());

        Point2D fromPoint = fxBord.sceneToLocal(
                fromBounds.getMinX() + fromBounds.getWidth() / 2,
                fromBounds.getMinY() + fromBounds.getHeight() / 2
        );

        Point2D toPoint = fxBord.sceneToLocal(
                toBounds.getMinX() + toBounds.getWidth() / 2,
                toBounds.getMinY() + toBounds.getHeight() / 2
        );

        Line line = new Line();
        line.setStartX(fromPoint.getX());
        line.setStartY(fromPoint.getY());
        line.setEndX(toPoint.getX());
        line.setEndY(toPoint.getY());

        line.setStrokeWidth(3);
        if (slangaStigi == 1) {
            line.setStroke(Color.DARKGREEN);
        }
        else {
            line.setStroke(Color.BROWN);
        }

        playerPane.getChildren().add(line);
    }

    private Node getNode(int tileNumber) {
        for (Node node : reitir) {
            if (node instanceof Label && ((Label) node).getText().equals(String.valueOf(tileNumber))) {
                return node;
            }
        }
        return null;
    }





}
