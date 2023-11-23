import java.time.LocalDate;
import java.util.ArrayList;

public class Database {

    // skal måske bruge flere filehandlers?
    // til at gemme svømmeresultat / konkurrencesvømmere
    FileHandler memberHandler;
    FileHandler resultHandler;
    FileHandler subscriptionHandler;
    private ArrayList<Member> memberList;
    private ArrayList<TournamentMember> tournamentMemberList;
    //private ArrayList<Subscription> subscriptionList;

    public Database() {
        memberHandler = new FileHandler("members.csv");
        resultHandler = new FileHandler("tournamentmembers.csv");
        subscriptionHandler = new FileHandler("subscription.csv");
        memberList = new ArrayList<>();
        memberList = memberHandler.loadMemberList();
        tournamentMemberList = new ArrayList<>();
        tournamentMemberList = resultHandler.loadTournamentMemberList();

    }

    public void saveList() {
        memberHandler.saveList(memberList);
    }

    // viser alle informationer om et givet medlem
    // skal måske skrives om til kun at vise relevant info
    public String showInfo(Member member) {
        String output = "";
        output = "\nNavn: " + member.getName()
                + "\nAlder: " + member.getAge()
                + "\nMail: " + member.getMail()
                + "\n";
        return output;
    }

    public ArrayList<Member> getMemberList() {
        return memberList;
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

}
