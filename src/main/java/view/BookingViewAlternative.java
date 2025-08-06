package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class BookingViewAlternative {

    /* -------------------- root -------------------- */
    private final VBox root = new VBox(24);

    /* -------------------- campi ------------------- */
    private final TextField firstNameField = new TextField();
    private final TextField lastNameField  = new TextField();
    private final TextField emailField     = new TextField();
    private final TextField phoneField     = new TextField();

    /* --- gruppi radio (ora anche le stanze) --- */
    private final ToggleGroup roomGroup        = new ToggleGroup();
    private final ToggleGroup destinationGroup = new ToggleGroup();
    private final ToggleGroup packageGroup     = new ToggleGroup();
    private final ToggleGroup activityGroup    = new ToggleGroup();

    /* --- contenitori radio --- */
    private final VBox roomBox        = new VBox(6);
    private final VBox destinationBox = new VBox(6);
    private final VBox packageBox     = new VBox(6);
    private final VBox activityBox    = new VBox(6);

    /* -------------------- bottoni ----------------- */
    private final Button confirmBtn = new Button("Conferma");
    private final Button cancelBtn  = new Button("Annulla");

    /* -------------------- errore ------------------ */
    private final Label errorLabel = new Label();

    /* ============================================================ */
    public BookingViewAlternative() {

        // Hook per CSS (il NavigationManager carica bw-mode.css)
        root.getStyleClass().add("booking-alt");

        // placeholder
        firstNameField.setPromptText("Es. Mario");
        lastNameField .setPromptText("Es. Rossi");
        emailField    .setPromptText("nome@dominio.it");
        phoneField    .setPromptText("Es. 3201234567");

        // sizing
        firstNameField.setPrefWidth(320);
        lastNameField .setPrefWidth(320);
        emailField    .setPrefWidth(320);
        phoneField    .setPrefWidth(320);

        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));

        /* ---------- form anagrafica (senza combobox stanza) ---------- */
        GridPane form = new GridPane();
        form.setHgap(12); form.setVgap(10);
        form.getColumnConstraints().addAll(
            new ColumnConstraints(110), new ColumnConstraints(360)
        );

        int r = -1;
        form.addRow(++r, new Label("Nome"),     firstNameField);
        form.addRow(++r, new Label("Cognome"),  lastNameField);
        form.addRow(++r, new Label("E-mail"),   emailField);
        form.addRow(++r, new Label("Telefono"), phoneField);
        // La scelta stanza è ora tra le sezioni radio (vedi sotto)

        /* ---------- sezioni radio (no pannelli bianchi) ---------- */
        VBox rooms       = section("Stanze",        roomBox);
        VBox destinations= section("Destinazioni",  destinationBox);
        VBox packages    = section("Pacchetti",     packageBox);
        VBox activities  = section("Attività",      activityBox);

        HBox radios = new HBox(32, rooms, destinations, packages, activities);
        radios.setAlignment(Pos.TOP_LEFT);

        /* ---------- footer ---------- */
        confirmBtn.setId("confirmBtn");
        cancelBtn .setId("cancelBtn");
        HBox buttons = new HBox(12, confirmBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        /* ---------- errore ---------- */
        errorLabel.getStyleClass().addAll("error", "error-message");
        errorLabel.setVisible(false);

        /* ---------- card ---------- */
        VBox card = new VBox(24, form, radios, errorLabel, buttons);
        card.getStyleClass().add("container");

        root.getChildren().add(card);
    }

    /** Sezione senza sfondo/bordi: titolo + contenuto radio. */
    private VBox section(String title, VBox content) {
        Label header = new Label(title);
        header.getStyleClass().add("section-title");

        content.getStyleClass().add("section-content");
        content.setPadding(new Insets(4, 6, 6, 6));

        VBox box = new VBox(6, header, content);
        box.getStyleClass().add("section");
        return box;
    }

    /* =================== API per Controller =================== */

    public VBox   getRoot()          { return root; }
    public Button getConfirmButton() { return confirmBtn; }
    public Button getCancelButton()  { return cancelBtn; }

    public String getFirstName()   { return firstNameField.getText().trim(); }
    public String getLastName()    { return lastNameField .getText().trim(); }
    public String getEmail()       { return emailField    .getText().trim(); }
    public String getPhoneNumber() { return phoneField    .getText().trim(); }

    public String getSelectedRoom()        { return selected(roomGroup); }
    public String getSelectedDestination() { return selected(destinationGroup); }
    public String getSelectedPackage()     { return selected(packageGroup); }
    public String getSelectedActivity()    { return selected(activityGroup); }

    private String selected(ToggleGroup g) {
        Toggle t = g.getSelectedToggle();
        return t == null ? null : ((RadioButton) t).getText();
    }

    /* ---- caricamento dati (senza listener) ---- */
    public void loadRooms(List<String> names)        { reloadRadios(names, roomGroup,        roomBox); }
    public void loadDestinations(List<String> names) { reloadRadios(names, destinationGroup, destinationBox); }
    public void loadPackages    (List<String> names) { reloadRadios(names, packageGroup,     packageBox); }
    public void loadActivities  (List<String> names) { reloadRadios(names, activityGroup,    activityBox); }

    private void reloadRadios(List<String> names, ToggleGroup group, VBox box) {
        box.getChildren().clear();
        group.selectToggle(null);
        for (String n : names) {
            RadioButton rb = new RadioButton(n);
            rb.setWrapText(true);
            rb.setToggleGroup(group);
            box.getChildren().add(rb);
        }
    }

    /* ---- errori + validazione ---- */
    public void hideAllErrors()           { errorLabel.setVisible(false); }
    public void setGeneralError(String m) { errorLabel.setText(m); errorLabel.setVisible(true); }

    public boolean validate() {
        hideAllErrors();
        if (getFirstName().isBlank())         { setGeneralError("Nome obbligatorio"); return false; }
        if (getLastName().isBlank())          { setGeneralError("Cognome obbligatorio"); return false; }
        if (getEmail().isBlank())             { setGeneralError("E-mail obbligatoria"); return false; }
        if (getPhoneNumber().isBlank())       { setGeneralError("Telefono obbligatorio"); return false; }
        if (getSelectedRoom() == null)        { setGeneralError("Seleziona una stanza"); return false; }
        if (getSelectedDestination() == null) { setGeneralError("Seleziona una destinazione"); return false; }
        if (getSelectedPackage() == null)     { setGeneralError("Seleziona un pacchetto"); return false; }
        if (getSelectedActivity() == null)    { setGeneralError("Seleziona un'attività"); return false; }
        return true;
    }
}
