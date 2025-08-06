package entity;



import java.time.LocalDate;
import java.time.LocalTime;

/**
 * POJO per le recensioni lasciate dagli utenti.
 */
public class Review {

    private int        id;
    private LocalDate  date;
    private LocalTime  time;
    private String     email;
    private int        stars;            // 1‒5
    private String     specialService;   // es. "Aperitivo"
    private String     body;             // testo della recensione (>= 250 parole)

    /* ---------------- getter/setter ---------------- */
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

    @Override
    public String toString() {
        return "Review[" + id + ", " + stars + "★, " + specialService +
               ", " + date + " " + time + ']';
    }
}