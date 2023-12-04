import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(mail, result.mail) &&
                Objects.equals(date, result.date) &&
                Objects.equals(time, result.time) &&
                Objects.equals(discipline, result.discipline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mail, date, time, discipline);
    }
}
