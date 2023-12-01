import java.time.LocalDate;

public class CompResult extends Result{

    String placement;
    String competition;

    public CompResult(String mail, LocalDate date, String time, String discipline, String placement, String competition) {
        super(mail, date, time, discipline);
        this.placement = placement;
        this.competition = competition;
    }

    public String getPlacement() {
        return placement;
    }

    public String getCompetition() {
        return competition;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }
}
