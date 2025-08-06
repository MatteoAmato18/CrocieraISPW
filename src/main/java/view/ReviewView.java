package view;



import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;

/**
 * View per l’inserimento di una recensione.
 */
public class ReviewView {

    /* ---------- nodi principali ---------- */
    private final VBox root = new VBox(15);

    private final TextArea        body = new TextArea();
    private final ComboBox<LocalTime> ora = new ComboBox<>();
    private final ToggleGroup     starsGroup   = new ToggleGroup();
    private final ToggleGroup     serviceGroup = new ToggleGroup();

    /* ---------- bottoni ---------- */
    private final Button sendButton   = new Button("Invia recensione");
    private final Button cancelButton = new Button("Annulla");

    /* ======================================================= */
    public ReviewView(List<String> specialServices) {
        root.setPrefSize(1280, 720);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding:32;");

        /* ----- campo testo (>= 250 parole) ----- */
        body.setPromptText("Scrivi qui la tua esperienza (almeno 250 parole) …");
        body.setWrapText(true);
        body.setPrefRowCount(10);

        /* ----- combo orari ----- */
        populateTimeBox();

        /* ----- stelle 1-5 ----- */
        HBox starsBox = new HBox(10);
        for (int i = 1; i <= 5; i++) {
            RadioButton rb = new RadioButton(i + " ★");
            rb.setToggleGroup(starsGroup);
            starsBox.getChildren().add(rb);
        }

        /* ----- servizi speciali ----- */
        VBox servBox = new VBox(5);
        Label servLabel = new Label("Servizio speciale utilizzato:");
        servBox.getChildren().add(servLabel);
        for (String s : specialServices) {
            RadioButton rb = new RadioButton(s);
            rb.setToggleGroup(serviceGroup);
            servBox.getChildren().add(rb);
        }

        /* ----- layout ----- */
        root.getChildren().addAll(
            new Label("Orario della visita"), ora,
            new Label("Valutazione"), starsBox,
            servBox,
            new Label("La tua recensione"), body,
            sendButton, cancelButton
        );
    }

    /* ======================================================= */
    /*                      API PUBLICA                        */
    /* ======================================================= */
    public VBox   getRoot()          { return root; }
    public Button getSendButton()    { return sendButton; }
    public Button getCancelButton()  { return cancelButton; }

    /* ----- valori inseriti dall’utente ----- */
    public LocalTime getTime() {
        return ora.getValue();
    }
    public int getStars() {
        RadioButton sel = (RadioButton) starsGroup.getSelectedToggle();
        return sel == null ? 0 : Integer.parseInt(sel.getText().substring(0,1));
    }
    public String getSelectedService() {
        RadioButton sel = (RadioButton) serviceGroup.getSelectedToggle();
        return sel == null ? null : sel.getText();
    }
    public String getBody() {
        return body.getText().trim();
    }

    /* ======================================================= */
    /*                      helper interni                     */
    /* ======================================================= */
    private void populateTimeBox() {
        var times = IntStream.rangeClosed(32, 95)   // 08:00 → 23:45
                             .mapToObj(i -> LocalTime.of(i / 4, (i % 4) * 15))
                             .toList();
        ora.setItems(FXCollections.observableArrayList(times));
        ora.setButtonCell(timeCell()); ora.setCellFactory(_ -> timeCell());
    }
    private ListCell<LocalTime> timeCell() {
        return new ListCell<>() {
            @Override protected void updateItem(LocalTime t, boolean empty) {
                super.updateItem(t, empty);
                setText(empty || t == null ? "" : t.toString());
            }
        };
    }
}