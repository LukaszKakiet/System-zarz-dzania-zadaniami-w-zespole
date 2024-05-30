import java.time.LocalDateTime;

public class Zadania {
    private String opis;
    private String status;
    private Czlonek_Zespolu przypisany_czlonek;
    private LocalDateTime czas_przypisania;
    private LocalDateTime czas_zakonczenia;
    
    public Zadania(String opis) {
        this.opis = opis;
        this.status = "Do zrobienia";
    }
    public String getOpis() {
        return opis;
    }
    public String getStatus() {
        return status;
    }
    public Czlonek_Zespolu getPrzypisany_Czlonek() {
        return przypisany_czlonek;
    }
    public LocalDateTime getCzas_Przyspisania() {
        return czas_przypisania;
    }
    public LocalDateTime getCzas_Zakonczenia() {
        return czas_zakonczenia;
    }
    public void przypisz_do(Czlonek_Zespolu czlonek) {
        this.przypisany_czlonek = czlonek;
        this.status = "W trakcie";
        this.czas_przypisania = LocalDateTime.now();
    }
    public void zakoncz() {
        this.status = "Zakończone";
        this.czas_zakonczenia = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return opis + " (" + status + ")";
    }
}
