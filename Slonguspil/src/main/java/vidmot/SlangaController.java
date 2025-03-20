package vidmot;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import vinnsla.Leikur;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SlangaController {

    @FXML
    private Button fxTeningurButton;
    private ImageView teningurImageView;

    @FXML
    private Button fxPlayButton;

    @FXML
    private Label fxSkilabod1;

    @FXML
    private Label fxSkilabod2;

    @FXML
    private TextField fxInput;

    @FXML
    private GridPane fxBord;

    private List<Node> reitir = new ArrayList<>();

    private Leikur leikur;
    private String nafn1, nafn2;
    private Boolean fyrstiLeikmadurInput = true;
    private ImageView leikmadur1Icon;
    private ImageView leikmadur2Icon;
    private ImageView slangaIcon;
    private ImageView stigiIcon;

    public void initialize(){

        leikur = new Leikur();
        fxInput.setPromptText("Leikmaður 1, sláðu inn nafn og ýttu á Enter:");
        leikur.LeikLokidProperty().set(true);

        reitir.clear();
        fxBord.getChildren().clear();

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


        placePlayer(leikmadur1Icon, 1);
        placePlayer(leikmadur2Icon, 1);
        fxInput.setOnKeyPressed(event ->{
            if(event.getCode() == KeyCode.ENTER){
                playerNameHandler();
            }
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
        fxTeningurButton.disableProperty().bind(fxPlayButton.disableProperty().not());

        //Tengja skilaboð labels við viðeigandi breytur
        fxSkilabod2.textProperty().bind(leikur.getSlongurStigar().FaersluSkilabodProperty());
        fxSkilabod1.textProperty().bind(
                Bindings.when(leikur.LeikLokidProperty())
                        .then(leikur.sigurvegariProperty())
                        .otherwise(leikur.hverALeikProperty()));



        //breyta mynd á tening
        leikur.teningurProperty().addListener((obs, oldVal, newVal) -> {
            String imagePath = "/vidmot/css/myndir/dice" + newVal + ".png";
            Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            teningurImageView.setImage(newImage);
            teningurImageView.setFitWidth(200);
            teningurImageView.setFitHeight(200);
            teningurImageView.setPreserveRatio(true);
            teningurImageView.setImage(newImage);
        });

    }

    public void playerNameHandler(){
        String inputNafn = fxInput.getText().trim();
        if(inputNafn.isEmpty()){
            fxInput.setPromptText("Nafn má ekki vera tómt! Reyndu aftur:");
            return;
        }
        if(fyrstiLeikmadurInput){
            nafn1 = inputNafn;
            fyrstiLeikmadurInput = false;
            fxInput.clear();
            fxInput.setPromptText("Leikmaður 2, sláðu inn nafn og ýttu á Enter:");
        }
        else {
            nafn2 = inputNafn;
            leikur.setLeikmenn(nafn1,nafn2);
            fxInput.clear();
            fxInput.setDisable(true);
            leikur.nyrLeikur(); //byrjum leikinn
            fxInput.setPromptText("...");

        }

    }

    public void teningurHandler(ActionEvent actionEvent){
        boolean gameOver = leikur.leikaLeik();
        if(gameOver){
            leikur.LeikLokidProperty().set(true);
        }
        int player1Pos = leikur.getLeikmadur1().getReitur();
        int player2Pos = leikur.getLeikmadur2().getReitur();

        fxBord.getChildren().remove(leikmadur1Icon);
        fxBord.getChildren().remove(leikmadur2Icon);

        placePlayer(leikmadur1Icon, player1Pos);
        placePlayer(leikmadur2Icon, player2Pos);

    }

    public void nyrLeikurHandler(ActionEvent actionEvent){
        leikur.nyrLeikur();
        fxBord.getChildren().remove(leikmadur1Icon);
        fxBord.getChildren().remove(leikmadur2Icon);

        placePlayer(leikmadur1Icon, 1);
        placePlayer(leikmadur2Icon, 1);
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

    private void placePlayer(ImageView playerIcon, int tileNumber) {
        for (Node node : reitir) {
            if (node instanceof Label && ((Label) node).getText().equals(String.valueOf(tileNumber))) {
                GridPane.setColumnIndex(playerIcon, GridPane.getColumnIndex(node));
                GridPane.setRowIndex(playerIcon, GridPane.getRowIndex(node));
                fxBord.getChildren().add(playerIcon);
                return;
            }
        }
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

}
