import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Member {

    private String name;
    private int age;
    private String mail;
    private boolean activeMembership;
    private LocalDate birthday;
    private LocalDate lastPaymentDate;
    private boolean isPaid;

    public Member(String name, String mail, boolean activeMembership, LocalDate birthday, LocalDate lastPaymentDate, boolean isPaid) {
        this.name = name;
        this.age = ageCalculator(birthday);
        this.mail = mail;
        this.activeMembership = activeMembership;
        this.birthday = birthday;
        this.lastPaymentDate = lastPaymentDate;
        this.isPaid = isPaid;
    }

    public static int ageCalculator(LocalDate birthday) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthday, currentDate);
        return period.getYears();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }

    public boolean isActiveMembership() {
        return activeMembership;
    }

    public String getMail() {
        return mail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setLastPaymentDate(LocalDate lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public void setActiveMembership(boolean activeMembership) {
        this.activeMembership = activeMembership;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    private void saveMemberListToFile() {
        Database db = new Database();
        db.saveMemberList();
    }

    public void updateLastPaymentDate() {
        this.lastPaymentDate = LocalDate.now();
        saveMemberListToFile();
    }


    public int calculateSubscription () {
        int discountPercentage = 25;
        int totalPercentage = 100;
        int discountCalculator = totalPercentage / discountPercentage;
        int subscriptionAmount = 0;

        if (!activeMembership) {
            subscriptionAmount = 500;
        }
        if (activeMembership && age < 18) {
            subscriptionAmount = 1000;
        }
        if (activeMembership && age > 18 && age < 60) {
            subscriptionAmount = 1600;

        } else if (activeMembership && age > 60) {
            subscriptionAmount = 1600 - 1600/discountCalculator;

        } return subscriptionAmount;
    }

    private void updatePaymentStatus() {
        LocalDate lastPaymentDate = getLastPaymentDate();
        LocalDate currentDate = LocalDate.now();
        if (lastPaymentDate.plusYears(1).isBefore(currentDate)) {
            isPaid = false;
        }
    }

    public boolean isPaid() {
        return isPaid;
    }

    public String getIsPaidString() {
        updatePaymentStatus();
        return isPaid ? "Ja" : "Nej";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return activeMembership == member.activeMembership &&
                isPaid == member.isPaid &&
                Objects.equals(name, member.name) &&
                Objects.equals(mail, member.mail) &&
                Objects.equals(birthday, member.birthday) &&
                Objects.equals(lastPaymentDate, member.lastPaymentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mail, activeMembership, birthday, lastPaymentDate, isPaid);
    }
}
