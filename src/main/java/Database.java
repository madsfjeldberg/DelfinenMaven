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
        fh.saveMemberList(memberList, "members.csv");
    }

    public void saveResultList() {
        fh.saveResultList(resultList, "results.csv");
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
        StringBuilder out = new StringBuilder();
        for (Member member : memberList) {
            out.append(showInfo(member));
        }
        return out.toString();
    }

    public void addMember(String name, String mail, boolean activeMembership,
                          LocalDate birthday, LocalDate lastPayment, boolean isPaid) {
        Member member = new Member(name, mail, activeMembership, birthday, lastPayment, true);
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

        for (Member member : memberList) {
            totalAmount += member.calculateSubscription();
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

    public ArrayList<Member> getUnpaidMember() {
        ArrayList<Member> unpaidMember = new ArrayList<>();
        int totalamount = 0;

        for (Member member : memberList) {
            if (!member.isPaid()) {
                unpaidMember.add(member);
                totalamount += member.calculateSubscription();
            }
        }
        System.out.println("Unpaid Members:");
        for (Member member : unpaidMember) {
            System.out.println("Navn: " + member.getName()
                    + ", Kontinget: " + member.calculateSubscription()
                    + ", Betalt: " + member.getIsPaidString());
        }

        System.out.println("Total manglende kontingent: " + totalamount);
        return unpaidMember;
    }

    public ArrayList<Member> getPaidMember() {
        ArrayList<Member> paidMember = new ArrayList<>();
        int totalamount = 0;

        for (Member member : memberList) {
            if (member.isPaid()) {
                paidMember.add(member);
                totalamount += member.calculateSubscription();
            }
        }
        System.out.println("Unpaid Members:");
        for (Member member : paidMember) {
            System.out.println("Navn: " + member.getName()
                    + ", Kontinget: " + member.calculateSubscription()
                    + ", Betalt: " + member.getIsPaidString());
        }

        System.out.println("Total manglende kontingent: " + totalamount);
        return paidMember;
    }
    public void updatePaymentForMember(String mail) {
        ArrayList<Member> memberList = getMemberList();

        for (Member member : memberList) {
            if (mail.equals(member.getMail())) {
                member.updateLastPaymentDate();
                member.setIsPaid(true);
                saveMemberList();
                return;
            }
        }

        System.out.println("Member not found.");
    }


}





