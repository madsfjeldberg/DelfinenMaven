import java.time.LocalDate;

public class Member {

    private String name;
    //TODO Skal redigere 'age'
    private int age;
    private String mail;
    private boolean activeMembership;
    private LocalDate birthday;
    private LocalDate lastPaymentDate;
    private LocalDate nextPaymentDate;
    private boolean isPaid;

    public Member(String name, int age, String mail, boolean activeMembership, LocalDate birthday, LocalDate lastPaymentDate, boolean isPaid) {
        this.name = name;
        this.age = age;
        this.mail = mail;
        this.activeMembership = activeMembership;
        this.birthday = birthday;
        this.lastPaymentDate = lastPaymentDate;
        this.nextPaymentDate = lastPaymentDate.plusYears(1);
        this.isPaid = isPaid;
    }

    public Member(String name, int age, String mail, boolean activeMembership, LocalDate birthday, LocalDate lastPaymentDate, LocalDate nextPaymentDate, boolean isPaid) {
        this.name = name;
        this.age = age;
        this.mail = mail;
        this.activeMembership = activeMembership;
        this.birthday = birthday;
        this.lastPaymentDate = lastPaymentDate;
        this.nextPaymentDate = nextPaymentDate;
        this.mail = mail;
        this.isPaid = isPaid;
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

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public boolean isActiveMembership() {
        return activeMembership;
    }

    public String getMail() {
        return mail;
    }

    public String getTeam(Member member) {
        if (activeMembership) {
            if (member.age < 18) {
                return "Junior";
            } else return "Senior";
        }
        return "Passiv";
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


    public String getIsPaidString() {
        if (isPaid) {
            return "Ja";
        } else return "Nej";
    }
}
