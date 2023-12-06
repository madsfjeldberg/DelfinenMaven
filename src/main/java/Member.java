import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Member {

    private final String name;
    private final int age;
    private String mail;
    private boolean activeMembership;
    private LocalDate birthday;
    private LocalDate lastPaymentDate;
    private boolean isPaid;
    private int phoneNumber;

    public Member(String name, String mail, boolean activeMembership, LocalDate birthday, LocalDate lastPaymentDate, int phoneNumber) {
        this.name = name;
        this.age = ageCalculator(birthday);
        this.mail = mail;
        this.activeMembership = activeMembership;
        this.birthday = birthday;
        this.lastPaymentDate = lastPaymentDate;
        this.isPaid = updatePaymentStatus();
        this.phoneNumber = phoneNumber;
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

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public void updateLastPaymentDate() {
        this.lastPaymentDate = LocalDate.now();
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

    private boolean updatePaymentStatus() {
        LocalDate lastPaymentDate = getLastPaymentDate();
        LocalDate currentDate = LocalDate.now();

        int lastPaymentYear = lastPaymentDate.getYear();
        int currentYear = currentDate.getYear();

        return !(lastPaymentYear == currentYear - 1 && lastPaymentDate.getMonthValue() < 9);
    }

    public boolean isPaid() {
        return isPaid;
    }

    public String getIsPaidString() {
        updatePaymentStatus();
        return isPaid ? "\u001B[32mJa\u001B[0m" : "\u001B[31mNej\u001B[0m";
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
