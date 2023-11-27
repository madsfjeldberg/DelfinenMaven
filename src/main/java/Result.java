import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Result {

    String mail;
    LocalDate date;
    String time;
    String discipline;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Result(String mail, LocalDate date, String time, String discipline) {
        this.mail = mail;
        this.date = date;
        this.time = time;
        this.discipline = discipline;
    }

    public String getMail() {
        return mail;
    }

    public String getDate() {
        return date.format(formatter);
    }

    public String getDiscipline() {
        return discipline;
    }

    public String getTime() {
        return time;
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
