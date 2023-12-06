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

    public ArrayList<Member> getMemberList() {
        return db.getMemberList();
    }

    public ArrayList<Result> getResultList() {
        return db.getResultList();
    }

    public void addMember(String name, String mail, boolean activeMembership, LocalDate birthday, LocalDate lastPayment, int phoneNumber) {
        db.addMember(name, mail, activeMembership, birthday, lastPayment, phoneNumber);
    }

    public void addResult(String mail, LocalDate date, String time, String discipline) {
        db.addResult(mail, date, time, discipline);
    }

    public void addCompResult(String mail, LocalDate date, String time, String discipline, String placement, String competition) {
        db.addCompResult(mail, date, time, discipline, placement, competition);
    }

    public String showSubscriptionList() {
        return db.showSubscriptionList();
    }

    public String getTotalSubscriptionAmount() {
        return db.getTotalSubscriptionAmount(); // Get the total amount from the database
    }
    public String getUnpaidMember() {
        return db.getUnpaidMember();
    }
    public String getPaidMember() {
        return db.getPaidMember();
    }
    public void updatePaymentForMember(String mail) {
        db.updatePaymentForMember(mail);
    }

    public String showTop5(boolean isCompetition, boolean isSenior){
        return db.showTop5(isCompetition, isSenior);
    }

}
