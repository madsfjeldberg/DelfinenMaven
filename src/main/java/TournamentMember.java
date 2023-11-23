import java.time.LocalDate;

public class TournamentMember extends Member {

    private Result result;
    private String team;

    public TournamentMember(String name, int age, String mail, boolean activeMembership,
                            LocalDate birthday, LocalDate lastPaymentDate, Result result, String team) {
        super(name, age, mail, activeMembership,birthday, lastPaymentDate);
        this.result = result;
        this.team = team;
    }
    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}