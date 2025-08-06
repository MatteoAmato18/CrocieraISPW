package view;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Variante “manuale”: l’utente scrive orario e stelle.
 */
public class ReviewAltView {

    /* ------------- nodi principali ------------- */
    private final VBox   root = new VBox(15);

    /* input “manuali” */
    private final TextField timeField  = new TextField();  // HH:mm
    private final TextField starsField = new TextField();  // 1–5

    private final TextArea  body = new TextArea();

    /* servizi speciali: RadioButton come prima */
    private final ToggleGroup servicesGroup = new ToggleGroup();
    private final VBox        servicesBox   = new VBox(5);

    /* bottoni */
    private final Button sendBtn   = new Button("Invia recensione");
    private final Button cancelBtn = new Button("Annulla");

    /* ------------ costruttore ------------ */
    public ReviewAltView(List<String> specialServices) {

        root.setPrefSize(1280, 720);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding:32;");

        timeField.setPromptText("Orario visita (es. 18:45)");
        starsField.setPromptText("Stelle da 1 a 5");

        body.setPromptText("Scrivi la tua esperienza (≥ 250 parole) …");
        body.setWrapText(true); body.setPrefRowCount(10);

        /* servizi speciali */
        Label servLabel = new Label("Servizio speciale utilizzato:");
        servicesBox.getChildren().add(servLabel);
        for (String s : specialServices) {
            RadioButton rb = new RadioButton(s);
            rb.setToggleGroup(servicesGroup);
            servicesBox.getChildren().add(rb);
        }

        root.getChildren().addAll(
            new Label("Orario della visita (inserisci manualmente)"), timeField,
            new Label("Valutazione stelle (1‒5)"), starsField,
            servicesBox,
            new Label("Recensione"), body,
            sendBtn, cancelBtn
        );
    }

    /* ------------- API pubblica ------------- */
    public VBox   getRoot()          { return root; }
    public Button getSendButton()    { return sendBtn; }
    public Button getCancelButton()  { return cancelBtn; }

    /* valori inseriti */
    public LocalTime getTime() {
        try { return LocalTime.parse(timeField.getText().trim()); }
        catch (Exception _) { return null; }
    }

    public int getStars() {
        try { return Integer.parseInt(starsField.getText().trim()); }
        catch (NumberFormatException _) { return 0; }
    }

    public String getSelectedService() {
        var sel = (RadioButton) servicesGroup.getSelectedToggle();
        return sel == null ? null : sel.getText();
    }

    public String getBody() { return body.getText().trim(); }
}