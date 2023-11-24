import java.time.LocalDate;
import java.util.ArrayList;

public class Database {

    FileHandler memberHandler;
    FileHandler resultHandler;
    FileHandler subscriptionHandler;
    private ArrayList<Member> memberList;
    private ArrayList<Result> resultList;
    //private ArrayList<Subscription> subscriptionList;

    public Database() {
        memberHandler = new FileHandler("members.csv");
        resultHandler = new FileHandler("results.csv");
        subscriptionHandler = new FileHandler("subscription.csv");
        memberList = new ArrayList<>();
        memberList = memberHandler.loadMemberList();
        resultList = resultHandler.loadResultList();

    }

    public void saveMemberList() {
        memberHandler.saveMemberList(memberList);
    }

    public void saveResultList() {
        resultHandler.saveResultList(resultList);
    }

    // viser alle informationer om et givet medlem
    // skal måske skrives om til kun at vise relevant info
    public String showInfo(Member member) {
        String output;
        output = "\nNavn: " + member.getName()
                + "\nAlder: " + member.getAge()
                + "\nMail: " + member.getMail()
                + "\n";
        return output;
    }

    public ArrayList<Member> getMemberList() {
        return memberList;
    }

    public ArrayList<Result> getResultList() {
        return resultList;
    }

    public String showList() {
        String out = "";
        for (Member member : memberList) {
            out += showInfo(member);
        }
        return out;
    }

    public void addMember(String name, int age, String mail, boolean activeMembership,
                          LocalDate birthday, LocalDate lastPayment) {
        Member member = new Member(name, age, mail, activeMembership, birthday, lastPayment);
        memberList.add(member);
    }

    public void addResult(String mail, LocalDate date, String time, String discipline) {
        Result result = new Result(mail, date, time, discipline);
        resultList.add(result);
    }

}
