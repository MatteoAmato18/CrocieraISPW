package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

public class BookingView {

    private final TextField firstName  = new TextField();
    private final TextField lastName   = new TextField();
    private final TextField email      = new TextField();
    private final TextField phone      = new TextField();
    private final ComboBox<String> roomCombo = new ComboBox<>();

    // solo contenitori degli item (niente label dentro)
    private final VBox destBox = new VBox(6);
    private final VBox packBox = new VBox(6);
    private final VBox actBox  = new VBox(6);

    private final ToggleGroup destGroup = new ToggleGroup();
    private final ToggleGroup packGroup = new ToggleGroup();
    private final ToggleGroup actGroup  = new ToggleGroup();

    private final Button confirmBtn = new Button("Conferma");
    private final Button cancelBtn  = new Button("Annulla");
    private final Label  errorLabel = new Label();

    private final VBox root = buildUI();

    private VBox buildUI() {

        firstName.setPromptText("Es. Mario");
        lastName .setPromptText("Es. Rossi");
        email    .setPromptText("nome@dominio.it");
        phone    .setPromptText("Es. 3201234567");
        roomCombo.setPromptText("Seleziona una stanza");

        // ----- form anagrafica
        GridPane form = new GridPane();
        form.setHgap(12); form.setVgap(10);
        form.addRow(0, new Label("Nome"),     firstName);
        form.addRow(1, new Label("Cognome"),  lastName);
        form.addRow(2, new Label("E-mail"),   email);
        form.addRow(3, new Label("Telefono"), phone);
        form.addRow(4, new Label("Stanza"),   roomCombo);
        form.getColumnConstraints().addAll(new ColumnConstraints(90), new ColumnConstraints());

        // ----- tre sezioni (titolo + lista radio), senza ScrollPane
        VBox destinations = section("Destinazioni", destBox);
        VBox packages     = section("Pacchetti",    packBox);
        VBox activities   = section("Attività",     actBox);

        HBox radios = new HBox(40, destinations, packages, activities);
        radios.setAlignment(Pos.TOP_LEFT);

        // ----- footer
        confirmBtn.setId("confirmBtn");
        cancelBtn .setId("cancelBtn");
        HBox buttons = new HBox(12, confirmBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        // ----- errore generale
        errorLabel.getStyleClass().add("error");
        errorLabel.setVisible(false);

        // ----- card + wrapper
        VBox card = new VBox(24, form, radios, errorLabel, buttons);
        card.getStyleClass().add("container");

        VBox wrapper = new VBox(card);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(40));
        var css = getClass().getResource("/style/booking_cruise.css");
        if (css != null) wrapper.getStylesheets().add(css.toExternalForm());

        return wrapper;
    }

    /** Crea una sezione “pulita” (titolo + contenitore radio) senza sfondo. */
    private VBox section(String title, VBox content) {
        Label header = new Label(title);
        header.getStyleClass().add("section-title");

        content.getStyleClass().add("section-content");     // solo per padding, nessuno sfondo
        content.setFillWidth(false);
        content.setPadding(new Insets(4, 6, 8, 6));

        VBox box = new VBox(6, header, content);
        box.getStyleClass().add("section");                 // nessuno sfondo/bordo
        return box;
    }

    // ===== API per il controller (nessun listener nella view) =====
    public VBox   getRoot()          { return root; }
    public Button getConfirmButton() { return confirmBtn; }
    public Button getCancelButton()  { return cancelBtn; }

    public String getFirstName()   { return firstName.getText().trim(); }
    public String getLastName()    { return lastName .getText().trim(); }
    public String getEmail()       { return email    .getText().trim(); }
    public String getPhoneNumber() { return phone    .getText().trim(); }

    public String getSelectedRoom()        { return roomCombo.getValue(); }
    public String getSelectedDestination() { return selected(destGroup); }
    public String getSelectedPackage()     { return selected(packGroup); }
    public String getSelectedActivity()    { return selected(actGroup); }

    private String selected(ToggleGroup g){
        Toggle t = g.getSelectedToggle();
        return t == null ? null : ((RadioButton)t).getText();
    }

    // Caricamento dati (no listener, no ScrollPane)
    public void loadRooms(List<String> rooms){
        roomCombo.getItems().setAll(rooms);
        roomCombo.getSelectionModel().clearSelection();
    }
    public void loadDestinations(List<String> names){ reloadRadios(names, destGroup, destBox); }
    public void loadPackages    (List<String> names){ reloadRadios(names, packGroup, packBox); }
    public void loadActivities  (List<String> names){ reloadRadios(names, actGroup , actBox ); }

    private void reloadRadios(List<String> names, ToggleGroup group, VBox box){
        box.getChildren().clear();
        group.selectToggle(null);
        for (String n : names) {
            RadioButton rb = new RadioButton(n);
            rb.setWrapText(true);
            rb.setToggleGroup(group);
            box.getChildren().add(rb);
        }
    }

    public void setError(String m){ errorLabel.setText(m); errorLabel.setVisible(true); }
    public void hideError()       { errorLabel.setVisible(false); }
    public void hideAllErrors()   { hideError(); }
    public void setGeneralError(String m){ setError(m); }

    public boolean validate() {
        hideError();
        if (getFirstName().isBlank())          { setError("Nome obbligatorio"); return false; }
        if (getLastName().isBlank())           { setError("Cognome obbligatorio"); return false; }
        if (getEmail().isBlank())              { setError("E-mail obbligatoria"); return false; }
        if (getPhoneNumber().isBlank())        { setError("Telefono obbligatorio"); return false; }
        if (getSelectedRoom() == null)         { setError("Seleziona una stanza"); return false; }
        if (getSelectedDestination() == null)  { setError("Seleziona una destinazione"); return false; }
        if (getSelectedPackage() == null)      { setError("Seleziona un pacchetto"); return false; }
        if (getSelectedActivity() == null)     { setError("Seleziona un'attività"); return false; }
        return true;
    }
}
