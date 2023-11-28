import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Database {

    FileHandler memberHandler;
    FileHandler resultHandler;
    FileHandler subscriptionHandler;
    private ArrayList<Member> memberList;
    private ArrayList<Result> resultList;
    private ArrayList<Subscription> subscriptionList;

    public Database() {
        memberHandler = new FileHandler("members.csv");
        resultHandler = new FileHandler("results.csv");
        subscriptionHandler = new FileHandler("subscription.csv");
        memberList = new ArrayList<>();
        memberList = memberHandler.loadMemberList();
        resultList = resultHandler.loadResultList();
        subscriptionList = new ArrayList<>();

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
    public ArrayList<Subscription> getSubscriptionList() {

        subscriptionList.clear();
        for (Member member: memberList) {
            int age = member.getAge();
            boolean activeMember = member.isActiveMembership();
            int subscriptionAmount = subscriptionCalculator(age, activeMember);

            Subscription subscription = new Subscription(member, false, subscriptionAmount);
            subscriptionList.add(subscription);
            
        } return subscriptionList;
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

    public static int ageCalculator(Member member) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthday = member.getBirthday();
        Period period = Period.between(birthday, currentDate);
        return period.getYears();

    }

    // kan skrives om til at tage member ind i stedet for specifikke attributer
    public int subscriptionCalculator(int age, boolean activeMember) {
            int discountPercentage = 25;
            int totalPercentage = 100;
            int discountCalculator = totalPercentage / discountPercentage;
            int subscriptionAmount = 0;

            if (!activeMember) {
                subscriptionAmount = 500;
            }
            if (activeMember && age < 18) {
                subscriptionAmount = 1000;
            }
            if (activeMember && age > 18 && age < 60) {
                subscriptionAmount = 1600;


            } else if (activeMember && age > 60) {
                subscriptionAmount = 1600 - 1600/discountCalculator;

            } return subscriptionAmount;
        }

    public int getTotalSubscriptionAmount() {
        int totalAmount = 0;

        for (Subscription subscription : subscriptionList) {
            totalAmount += subscription.getSubscriptionAmount();
        }
        return totalAmount;
    }

    public ArrayList<Subscription> getUnpaidSubscriptions() {
        ArrayList<Subscription> unpaidSubscriptions = new ArrayList<>();

        for (Subscription subscription : subscriptionList) {
            if (!subscription.getisPaid()) {
                unpaidSubscriptions.add(subscription);
            }
        }
        return unpaidSubscriptions;
    }
}





