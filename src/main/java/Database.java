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
                          LocalDate birthday, LocalDate lastPayment) {
        Member member = new Member(name, mail, activeMembership, birthday, lastPayment);
        memberList.add(member);
    }

    public void addResult(String mail, LocalDate date, String time, String discipline) {
        Result result = new Result(mail, date, time, discipline);
        resultList.add(result);
    }

    // udregner alder på baggrund af fødselsdag
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

    public String showSubscriptionList() {

        StringBuilder out = new StringBuilder();
        for (Member member : memberList) {
            out.append("Navn: ")
                    .append(member.getName())
                    .append(", Kontingent: ")
                    .append(member.calculateSubscription())
                    .append(", Betalt: ")
                    .append(member.getIsPaidString())
                    .append("\n");
        }
        return out.toString();
    }

    public String getUnpaidMember() {
        ArrayList<Member> unpaidMember = new ArrayList<>();
        int totalamount = 0;

        for (Member member : memberList) {
            if (!member.isPaid()) {
                unpaidMember.add(member);
                totalamount += member.calculateSubscription();
            }
        }
        StringBuilder out = new StringBuilder();
        for (Member member : unpaidMember) {
            out.append("Navn: ")
                    .append(member.getName())
                    .append("\nKontingent: ")
                    .append(member.calculateSubscription())
                    .append("\nBetalt: ")
                    .append(member.getIsPaidString())
                    .append("\nMail: ")
                    .append(member.getMail())
                    .append("\n\n");
        }

        return "Medlemmer der ikke har betalt:\n" + out + "\nTotal manglende kontingent: " + totalamount + "\n";
    }

    public String getPaidMember() {
        ArrayList<Member> paidMember = new ArrayList<>();
        int totalamount = 0;

        for (Member member : memberList) {
            if (member.isPaid()) {
                paidMember.add(member);
                totalamount += member.calculateSubscription();
            }
        }
        StringBuilder out = new StringBuilder();
        for (Member member : paidMember) {

            out.append("Navn: ").append(member.getName())
                    .append("\nKontingent: ")
                    .append(member.calculateSubscription())
                    .append("\nBetalt: ")
                    .append(member.getIsPaidString())
                    .append("\nMail: ")
                    .append(member.getMail())
                    .append("\n\n");
        }

        return "Medlemmer der har betalt:\n" + out + "\nTotal indbetalt kontingent: " + totalamount + "\n";
    }

    public String updatePaymentForMember(String mail) {
        ArrayList<Member> memberList = getMemberList();

        for (Member member : memberList) {
            if (mail.equals(member.getMail())) {
                member.updateLastPaymentDate();
                member.setIsPaid(true);
                saveMemberList();
                return "Medlem opdateret.";
            }
        }
        return "Medlem ikke fundet.";
    }
}





