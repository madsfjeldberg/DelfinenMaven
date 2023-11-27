import java.time.LocalDate;

public class Subscription {
    private Member member;
    private boolean isPaid;
    private int subscriptionAmount;

    public Subscription(Member member, boolean isPaid, int subscriptionAmount) {
        this.member = member;
        this.isPaid = isPaid;
        this.subscriptionAmount = subscriptionAmount;
    }
    public Member getMember() {
        return member;
    }


    public int getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public boolean getisPaid() {
        return isPaid;
    }
    public String getIsPaidString() {
        updatePaymentStatus();
        return isPaid ? "Ja" : "Nej";
    }
    private void updatePaymentStatus() {
        LocalDate lastPaymentDate = member.getLastPaymentDate();
        LocalDate currentDate = LocalDate.now();
        if (lastPaymentDate.plusYears(1).isBefore(currentDate)) {
            isPaid = false;
        }
    }
    public void setAmount () {
    }

    public void setisPaid(boolean isPaid) {
    }
}
