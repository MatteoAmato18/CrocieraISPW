package bean;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO per il flusso View ↔ Service delle recensioni.
 */
public class ReviewBean {

    private int        id;
    private LocalDate  date;
    private LocalTime  time;
    private String     email;
    private int        stars;            // 1–5
    private String     specialService;   // facoltativo
    private String     body;             // testo (>= 250 parole)

    /* ================== mini-validazioni ================== */
    public boolean hasValidStars() { return stars >= 1 && stars <= 5; }

    /** Almeno 250 parole reali (token separati da spazi). */
    public boolean hasValidBody() {
        return body != null &&
               body.strip().split("\\s+").length >= 10;
    }

    public boolean hasValidDateTime() {
        return date != null && time != null &&
               !date.isAfter(LocalDate.now());      // non nel futuro
    }

    /* ================== getter / setter =================== */
    public int        getId()                     { return id; }
    public void       setId(int id)               { this.id = id; }

    public LocalDate  getDate()                   { return date; }
    public void       setDate(LocalDate date)     { this.date = date; }

    public LocalTime  getTime()                   { return time; }
    public void       setTime(LocalTime time)     { this.time = time; }

    public String     getEmail()                  { return email; }
    public void       setEmail(String email)      { this.email = email; }

    public int        getStars()                  { return stars; }
    public void       setStars(int stars)         { this.stars = stars; }

    public String     getSpecialService()         { return specialService; }
    public void       setSpecialService(String s) { this.specialService = s; }

    public String     getBody()                   { return body; }
    public void       setBody(String body)        { this.body = body; }
}