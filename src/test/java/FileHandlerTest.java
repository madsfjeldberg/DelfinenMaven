import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class FileHandlerTest {

    FileHandler fh;

    @BeforeEach
    void setup() {
        fh = new FileHandler();
    }

    @Test
    void saveMemberList() {
        ArrayList<Member> memberList = new ArrayList<>();
        Member member1 = new Member("John", "john@doe", true, LocalDate.of(1990, 1, 1), LocalDate.of(2022, 2, 2), true);
        Member member2 = new Member("Jane", "jane@doe", false, LocalDate.of(1995, 5, 5), LocalDate.of(2021, 1, 1), false);
        memberList.add(member1);
        memberList.add(member2);

        fh.saveMemberList(memberList, "membersTest.csv");

        ArrayList<Member> loadedMemberList = fh.loadMemberList("membersTest.csv");
        assertEquals(memberList, loadedMemberList);
    }

    @Test
    void saveResultList() {
        ArrayList<Result> resultList = new ArrayList<>();
        Result result1 = new Result("mail", LocalDate.of(2022, 2, 2), "10:10:10", "crawl");
        Result result2 = new Result("mail", LocalDate.of(2022, 2, 2), "10:10:10", "crawl");

        resultList.add(result1);
        resultList.add(result2);

        fh.saveResultList(resultList, "resultTest.csv");

        ArrayList<Result> loadedResultList = fh.loadResultList("resultTest.csv");
        assertEquals(resultList, loadedResultList);
    }

    @Test
    void loadResultList() {
        ArrayList<Result> resultList = new ArrayList<>();
        Result result1 = new Result("mail", LocalDate.of(2022, 2, 2), "10:10:10", "crawl");
        Result result2 = new Result("mail", LocalDate.of(2022, 2, 2), "10:10:10", "crawl");

        resultList.add(result1);
        resultList.add(result2);

        fh.saveResultList(resultList, "resultTest.csv");

        ArrayList<Result> loadedResultList = fh.loadResultList("resultTest.csv");
        assertEquals(resultList, loadedResultList);
    }

    @Test
    void loadMemberList() {
        ArrayList<Member> memberList = new ArrayList<>();
        Member member1 = new Member("John", "john@doe", true, LocalDate.of(1990, 1, 1), LocalDate.of(2022, 2, 2), true);
        Member member2 = new Member("Jane", "jane@doe", false, LocalDate.of(1995, 5, 5), LocalDate.of(2021, 1, 1), false);
        memberList.add(member1);
        memberList.add(member2);

        fh.saveMemberList(memberList, "membersTest.csv");

        ArrayList<Member> loadedMemberList = fh.loadMemberList("membersTest.csv");
        assertEquals(memberList, loadedMemberList);
    }
}
