public class Czlonek_Zespolu {
    private String nazwa;

    public Czlonek_Zespolu(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getNazwa() {
        return nazwa;
    }

    @Override
    public String toString() {
        return nazwa;
    }
}