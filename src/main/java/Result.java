import java.time.LocalDate;

public class Result {

    LocalDate date;
    String time;
    String discipline;

    public Result(LocalDate date, String time, String discipline) {
        this.date = date;
        this.time = time;
        this.discipline = discipline;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDiscipline() {
        return discipline;
    }

    public String getTime() {
        return time;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Result{" +
                "date=" + date +
                ", time='" + time + '\'' +
                ", discipline='" + discipline + '\'' +
                '}';
    }
}
