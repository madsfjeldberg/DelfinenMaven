import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseTest {

    Database db;
    FileHandler fh;
    ArrayList<Member> memberList;
    ArrayList<Result> resultList;
    DateTimeFormatter formatter;

    @BeforeEach
    void setup() {
        db = new Database();
        fh = new FileHandler();
        memberList = new ArrayList<>();
        resultList = new ArrayList<>();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        db.getMemberList().clear();
    }

    @Test
    void showInfo() {
        Member testMember = new Member("name", "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29),12345678);

        String actual = db.showInfo(testMember);

        String expected = """
                
                Navn: name
                Alder: 28
                Mail: mail
                """;
        assertEquals(expected, actual);
    }

    @Test
    void addMember() {

        Member testMember = new Member("name", "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29),12345678);

        ArrayList<Member> memberList = new ArrayList<>();
        memberList.add(testMember);

        assertTrue(memberList.stream().anyMatch(member ->
                testMember.getName().equals("name") &&
                        testMember.getAge() == (28) &&
                        testMember.getMail().equals("mail") &&
                        testMember.isActiveMembership() &&
                        testMember.getBirthday().equals(LocalDate.of(1995, 4, 29)) &&
                        testMember.getLastPaymentDate().equals(LocalDate.of(1995, 4, 29))));
    }

    @Test
    void showList() {

        Member testMember = new Member("name", "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29), 12345678);
        db.getMemberList().clear();

        db.addMember("name", "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29),12345678);
        memberList.add(testMember);
        String actual = db.showList();
        String expected = """
                
                Navn: name
                Alder: 28
                Mail: mail
                """;
        assertEquals(actual, expected);
    }

    @Test
    void addResult() {

        Result testResult = new Result("mail", LocalDate.of(1995, 4, 29), "2:23:03", "crawl");

        ArrayList<Result> resultList = new ArrayList<>();
        resultList.add(testResult);

        assertTrue(resultList.stream().anyMatch(result ->
                testResult.getMail().equals("mail") &&
                        testResult.getDate().equals("29-04-1995") &&
                        testResult.getTime().equals("2:23:03") &&
                        testResult.getDiscipline().equals("crawl")));
    }

    @Test
    void ageCalculator() {
        LocalDate currentDate = LocalDate.now();
        Member testMember = new Member("name", "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29),12345678);

        int actual = db.ageCalculator(testMember);
        Period period = Period.between(LocalDate.of(1995, 4, 29), currentDate);
        int expected = period.getYears();
        assertEquals(actual, expected);
    }

    @Test
    void getTotalSubscriptionAmount() {

        Member testMember = new Member("name", "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29),12345678);
        Member testMember1 = new Member("name", "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29),12345678);
        Member testMember2 = new Member("name", "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29),12345678);

        db.getMemberList().clear();
        db.getMemberList().add(testMember);
        db.getMemberList().add(testMember1);
        db.getMemberList().add(testMember2);

        int expected = 4800;
        int actual = db.getTotalSubscriptionAmount();

        assertEquals(expected, actual);
    }
}

