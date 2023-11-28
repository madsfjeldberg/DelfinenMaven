import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class Database {

    FileHandler fh;
    private ArrayList<Member> memberList;
    private ArrayList<Result> resultList;

    public Database() {
        fh = new FileHandler();
        memberList = new ArrayList<>();
        memberList = fh.loadMemberList("members.csv");
        resultList = fh.loadResultList("results.csv");

    }

    public void saveMemberList() {
        fh.saveMemberList(memberList);
    }

    public void saveResultList() {
        fh.saveResultList(resultList);
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
                          LocalDate birthday, LocalDate lastPayment, boolean isPaid) {
        Member member = new Member(name, age, mail, activeMembership, birthday, lastPayment, true);
        memberList.add(member);
    }

    public void addResult(String mail, LocalDate date, String time, String discipline) {
        Result result = new Result(mail, date, time, discipline);
        resultList.add(result);
    }

    public static int ageCalculator(Member member) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthday = member.getBirthday();
        Period period = Period.between(birthday, currentDate);
        return period.getYears();

    }

    public int getTotalSubscriptionAmount() {
        int totalAmount = 0;

        for (Member subscription : memberList) {
            totalAmount += subscription.calculateSubscription();
        }
        return totalAmount;
    }
    public void showSubscriptionList() {

        for (Member member : memberList) {
            System.out.println("Navn: " + member.getName()
                    + ", Kontingent: " + member.calculateSubscription()
                    + ", Betalt: " + member.getIsPaidString());
        }
    }

    /*public ArrayList<Subscription> getUnpaidSubscriptions() {
        ArrayList<Subscription> unpaidSubscriptions = new ArrayList<>();

        for (Member subscription : memberList) {
            if (!subscription.getisPaid()) {
                unpaidSubscriptions.add(subscription);
            }
        }
        return unpaidSubscriptions;
    }*/


}





