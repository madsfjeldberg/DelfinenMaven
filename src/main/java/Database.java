import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Database {

    FileHandler fh;
    private ArrayList<Member> memberList;
    private final ArrayList<Result> resultList;

    public Database(String members, String results) {
        fh = new FileHandler();
        memberList = new ArrayList<>();
        memberList = fh.loadMemberList(members);
        resultList = fh.loadResultList(results);
    }

    public void saveMemberList() {
        fh.saveMemberList(memberList, "members.csv");
    }

    public void saveResultList() {
        fh.saveResultList(resultList, "results.csv");
    }

    public String showInfo(Member member) {
        return String.format("| %-20s | %-10s | %-30s | %-15s |\n",
                member.getName(), member.getAge(), member.getMail(), member.getPhoneNumber());
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
                          LocalDate birthday, LocalDate lastPayment, int phoneNumber) {
        Member member = new Member(name, mail, activeMembership, birthday, lastPayment, phoneNumber);
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

    public String getTotalSubscriptionAmount() {
        int totalAmount = 0;

        int currentYear = LocalDate.now().getYear();



        for (Member member : memberList) {
            if (member.getLastPaymentDate().getYear() == currentYear){
                totalAmount += member.calculateSubscription();
            }
        }
        String total = String.valueOf(totalAmount);
        return colorize(total, "GREEN");
    }

    public String accountMenu() {
        return "─".repeat(130) +
                "\n" +
                String.format("| %-18s | %-10s | %-25s | %-13s | %-12s | %-14s |\n", "Navn", "Alder", "Mail", "Telefon nr.", "Beløb i kr.", "Betalt Ja/Nej") +
                "─".repeat(130) +
                "\n";
    }

    public String showInfoSubscription(Member member) {
        return String.format("| %-18s | %-10s | %-25s | %-13s | %-12s | %-23s |\n",
                member.getName(), member.getAge(), member.getMail(), member.getPhoneNumber(), member.calculateSubscription() + " kr.", member.getIsPaidString());
    }

    public String showSubscriptionList() {

        StringBuilder output = new StringBuilder();
        output.append(accountMenu());
        for (Member member : memberList) {

            output.append(showInfoSubscription(member));

        }
        return output.toString();
    }

    private String colorize(String text, String color) {

        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";

        return switch (color.toUpperCase()) {
            case "GREEN" -> ANSI_GREEN + text + ANSI_RESET;
            case "RED" -> ANSI_RED + text + ANSI_RESET;
            default -> text;
        };
    }

    public String getUnpaidMember() {
        ArrayList<Member> unpaidMember = new ArrayList<>();
        int totalAmount = 0;

        for (Member member : memberList) {
            if (!member.isPaid()) {
                unpaidMember.add(member);
                totalAmount += member.calculateSubscription();
            }
        }

        StringBuilder output = new StringBuilder();
        output.append(accountMenu());
        for (Member member : unpaidMember) {
           output.append(showInfoSubscription(member));
        }

        output.append("\n Manglende indtægt fra betalende medlemmer: ").append(colorize(String.valueOf(totalAmount), "RED")).append(" kr.\n");
        return output.toString();
    }

    public String getPaidMember() {
        ArrayList<Member> paidMember = new ArrayList<>();
        int totalAmount = 0;

        for (Member member : memberList) {
            if (member.isPaid()) {
                paidMember.add(member);
                totalAmount += member.calculateSubscription();
            }
        }
        StringBuilder output = new StringBuilder();
        output.append(accountMenu());
        for (Member member : paidMember) {
            output.append(showInfoSubscription(member));
        }
        output.append("\n Indtægt fra betalende medlemmer: ").append(colorize(String.valueOf(totalAmount), "GREEN")).append(" kr.");
        return output.toString();
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
    public String showTop5(boolean isCompetition, boolean isSenior) {
        StringBuilder resultStringBuilder = new StringBuilder();

        resultStringBuilder.append(String.format("Top 5 %stider - Alle discipliner:\n", isCompetition ? "Turnerings" : "Trænings"));
        resultStringBuilder.append("─".repeat(100));
        resultStringBuilder.append("\n");
        resultStringBuilder.append(String.format("| %-10s | %-15s | %-13s | %-13s | %-10s | %-20s |", "Placering", "Navn", "Tid", "Dato", "Gruppe", "Stævne"));
        resultStringBuilder.append("\n");
        resultStringBuilder.append("─".repeat(100));

        for (String swimStyle : List.of("crawl", "rygcrawl", "bryst", "butterfly")) {
            List<Result> filteredResults = resultList.stream()
                    .filter(result -> result instanceof CompResult == isCompetition)
                    .filter(result -> isSenior == (ageCalculator(getMemberByEmail(result.getMail())) >= 18))
                    .filter(result -> result.getDiscipline().equalsIgnoreCase(swimStyle))
                    .sorted(Comparator.comparing(Result::getTime))
                    .toList();

            if (!filteredResults.isEmpty()) {
                resultStringBuilder.append("\n");
                resultStringBuilder.append(String.format("%52s", swimStyle.toUpperCase()));
                resultStringBuilder.append("\n");
                for (int i = 0; i < Math.min(5, filteredResults.size()); i++) {
                    Result result = filteredResults.get(i);
                    String ageGroup = isSenior ? "Senior" : "Junior";

                    Member member = getMemberByEmail(result.getMail());

                    if (member != null) {

                         resultStringBuilder.append(String.format("\n| %-10s | %-15s | %-13s | %-13s | %-10s | %-20s |",
                                 ((CompResult) result).getPlacement(), member.getName(), result.getTime(), result.getDate(), ageGroup, ((CompResult) result).getCompetition()));
                    }
                }
                resultStringBuilder.append("\n");
                resultStringBuilder.append("─".repeat(100));
            }
        }

        return resultStringBuilder.toString();
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





