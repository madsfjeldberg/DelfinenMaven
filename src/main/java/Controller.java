import java.time.LocalDate;
import java.util.ArrayList;

public class Controller {

    private final Database db;

    public Controller() {
        db = new Database();
    }

    public void saveMemberList() {
        db.saveMemberList();
    }

    public void saveResultList() {
        db.saveResultList();
    }

    public String showList() {
        return db.showList();
    }

    public String showInfo(Member member) {
        return db.showInfo(member);
    }

    public ArrayList<Member> getMemberList() {
        return db.getMemberList();
    }

    public ArrayList<Result> getResultList() {
        return db.getResultList();
    }

    public void addMember(String name, int age, String mail, boolean activeMembership, LocalDate birthday, LocalDate lastPayment) {
        db.addMember(name, age, mail, activeMembership, birthday, lastPayment);
    }

    public void addResult(String mail, LocalDate date, String time, String discipline) {
        db.addResult(mail, date, time, discipline);
    }
    public void showSubscriptionList() {
        ArrayList<Subscription> subscriptionList = db.getSubscriptionList();
        for (Subscription subscription : subscriptionList) {
            System.out.println("Navn: " + subscription.getMember().getName()
                    + ", Kontinget: " + subscription.getSubscriptionAmount()
                    + ", Betalt: " + subscription.getIsPaidString());
        }
    }
    public int getTotalSubscriptionAmount() {
        int totalAmount = db.getTotalSubscriptionAmount(); // Get the total amount from the database


        return totalAmount;
    }
}
