import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    /*
    public String showInfo(Member member) {
        String output;
        output = "\nNavn: " + member.getName()
                + "\nAlder: " + member.getAge()
                + "\nMail: " + member.getMail()
                + "\n";
        return output;
    }

     */

    public String showInfo(Member member) {
        StringBuilder output = new StringBuilder();
        output.append(String.format("| %-20s | %-10s | %-30s | %-15s |\n",
                member.getName(), member.getAge(), member.getMail(), "tlf nr. her"));
        return output.toString();
    }

    public ArrayList<Member> getMemberList() {
        return memberList;
    }

    public ArrayList<Result> getResultList() {
        return resultList;
    }

    public String showList() {
        StringBuilder output = new StringBuilder();
        output.append("─".repeat(90));
        output.append("\n");
        output.append(String.format("| %-20s | %-10s | %-30s | %-15s |\n", "Navn", "Alder", "Mail", "Telefon nr."));
        output.append("─".repeat(90));
        output.append("\n");


        for (Member member : memberList) {
            output.append(showInfo(member));
        }
        return output.toString();
    }

    public void addMember(String name, String mail, boolean activeMembership,
                          LocalDate birthday, LocalDate lastPayment) {
        Member member = new Member(name, mail, activeMembership, birthday, lastPayment);
        memberList.add(member);
    }

    public void addCompResult(String mail, LocalDate date, String time, String discipline, String placement, String competition) {
        CompResult result = new CompResult(mail, date, time, discipline, placement, competition);
        resultList.add(result);
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

        int currentYear = LocalDate.now().getYear();

        for (Member member : memberList) {
            if (member.getLastPaymentDate().getYear() == currentYear){
                totalAmount += member.calculateSubscription();
            }
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
        return "Medlemmer der ikke har betalt:\n" + out + "\nTotal manglende kontingent: " + "\u001B[31m" + totalamount +"\u001B[0m" + "\n";
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

        return "Medlemmer der har betalt:\n" + out + "\nTotal indbetalt kontingent: " + "\u001B[32m" + totalamount + "\u001B[0m" + "\n";
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
    public void showTop5(boolean isCompetition, boolean isSenior, String swimStyle) {
        List<Result> filteredResults = resultList.stream()
                .filter(result -> result instanceof CompResult == isCompetition)
                .filter(result -> isSenior == (ageCalculator(getMemberByEmail(result.getMail())) >= 18))
                .filter(result -> result.getDiscipline().equalsIgnoreCase(swimStyle)).sorted(Comparator.comparing(Result::getTime)).toList();

        System.out.printf("Top 5 %stid - %s\n", isCompetition ? "Turnerings" : "Trænings", swimStyle);
        System.out.println("--------------------------------------------------------");
        for (int i = 0; i < Math.min(5, filteredResults.size()); i++) {
            Result result = filteredResults.get(i);
            String resultType = isCompetition ? "Turnerings" : "Trænings";
            String ageGroup = isSenior ? "Senior" : "Junior";

            Member member = getMemberByEmail(result.getMail());

            if (member != null) {
                System.out.printf("%d. %stid: %s, Dato: %s, %s, %s\n",
                        i + 1, resultType, result.getTime(), result.getDate(), ageGroup, member.getName());
            } else {
                System.out.printf("%d. %stid: %s, Dato: %s, %s, Medlem ikke fundet\n",
                        i + 1, resultType, result.getTime(), result.getDate(), ageGroup);
            }
        }
        System.out.println("--------------------------------------------------------");
    }

    public Member getMemberByEmail(String email) {
        for (Member member : memberList) {
            if (member.getMail().equalsIgnoreCase(email)) {
                return member;
            }
        }
        return null;
    }

}





