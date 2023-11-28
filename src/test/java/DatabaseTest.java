import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseTest {

    Database db;
    FileHandler memberHandler;
    FileHandler resultHandler;
    ArrayList<Member> memberList;
    ArrayList<Result> resultList;
    ArrayList<Subscription> subscriptionList;
    DateTimeFormatter formatter;

    @BeforeEach
    void setup() {
        db = new Database();
        memberHandler = new FileHandler("membersTest.csv");
        resultHandler = new FileHandler("resultTest.csv");
        memberList = new ArrayList<>();
        resultList = new ArrayList<>();
        subscriptionList = new ArrayList<>();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    }

    @Test
    void showInfo() {
        Member testMember = new Member("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));

        String actual = db.showInfo(testMember);

        String expected = """
                Navn: name
                Alder: 28
                Mail: mail
                """;
        assertEquals(expected, actual);
    }

    // TODO: evt check på indhold i stedet for størrelse.
    @Test
    void getSubscriptionList() {

        Member testMember = new Member("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));
        Member testMember1 = new Member("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));
        Member testMember2 = new Member("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));

        Subscription subscription = new Subscription(testMember, false, db.subscriptionCalculator(testMember.getAge(), testMember.isActiveMembership()));
        Subscription subscription1 = new Subscription(testMember1, false, db.subscriptionCalculator(testMember1.getAge(), testMember1.isActiveMembership()));
        Subscription subscription2 = new Subscription(testMember2, false, db.subscriptionCalculator(testMember2.getAge(), testMember2.isActiveMembership()));

        subscriptionList.add(subscription);
        subscriptionList.add(subscription1);
        subscriptionList.add(subscription2);

        ArrayList<Subscription> expected = new ArrayList<>();
        expected.add(subscription);
        expected.add(subscription1);
        expected.add(subscription2);

        ArrayList<Subscription> actual = db.getSubscriptionList();

        assertEquals(expected.size(), actual.size());
    }


    @Test
    void addMember() {

        Member testMember = new Member("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));


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

        Member testMember = new Member("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));
        db.getMemberList().clear();

        db.addMember("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));
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
        Member testMember = new Member("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));

        int actual = db.ageCalculator(testMember);
        Period period = Period.between(LocalDate.of(1995, 4, 29), currentDate);
        int expected = period.getYears();
        assertEquals(actual, expected);
    }

    @Test
    void subscriptionCalculator() {
        Member testMemberSenior = new Member("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));

        int ageSenior = testMemberSenior.getAge();
        boolean activeMember = testMemberSenior.isActiveMembership();

        int discountPercentage = 25;
        int totalPercentage = 100;
        int discountCalculator = totalPercentage / discountPercentage;
        int subscriptionAmount = 0;

        if (!activeMember) {
            subscriptionAmount = 500;
        }
        if (activeMember && testMemberSenior.getAge() < 18) {
            subscriptionAmount = 1000;
        }
        if (activeMember && testMemberSenior.getAge() > 18 && testMemberSenior.getAge() < 60) {
            subscriptionAmount = 1600;


        } else if (activeMember && testMemberSenior.getAge() > 60) {
            subscriptionAmount = 1600 - 1600 / discountCalculator;

            int actual = db.subscriptionCalculator(ageSenior, activeMember);
            int expected = 1600;

            assertEquals(expected, actual);

        }
    }

    @Test
    void getTotalSubscriptionAmount() {

        Member testMember = new Member("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));
        Member testMember1 = new Member("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));
        Member testMember2 = new Member("name", 28, "mail", true, LocalDate.of(1995, 4, 29), LocalDate.of(1995, 4, 29));

        Subscription sub = new Subscription(testMember, false, 1600);
        Subscription sub1 = new Subscription(testMember1, false, 1600);
        Subscription sub2 = new Subscription(testMember2, false, 1600);

        db.getSubscriptionList().add(sub);
        db.getSubscriptionList().add(sub1);
        db.getSubscriptionList().add(sub2);

        int expected = 6000;
        int actual = db.getTotalSubscriptionAmount();

        assertEquals(expected, actual);
    }
}

