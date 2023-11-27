import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterface {

    private final Controller ctrl;
    private final Scanner input;
    int userInput;
    DateTimeFormatter formatter;

    public UserInterface() {
        ctrl = new Controller();
        input = new Scanner(System.in);
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public void run() {

        do {
            displayMainMenu();
            userInput = getValidInput();
            switch (userInput) {
                case 1 -> chairmanMenu();
                case 2 -> treasurerMenu();
                case 3 -> trainerMenu();
                case 9 -> System.out.println("Afslutter programmet. Farvel!");
            }
        } while (userInput != 9);
    }

    private void trainerMenu() {

        do {
            System.out.println("""
                    Træner Menu
                    1. Top 5 svømmere (IKKE IMPLEMENTERET)
                    2. Opdater resultat (BETA)
                    3. Søg efter svømmer (BETA)
                    9. Tilbage til hovedmenu""");

            userInput = getValidInput();

            switch (userInput) {
                case 1 -> top5();
                case 2 -> update();
                case 3 -> coachSearch();
                case 9 -> {
                    System.out.println("Tilbage til hovedmenuen.");
                    ctrl.saveMemberList();
                    ctrl.saveResultList();
                    run();
                }
                default -> System.out.println("Fejl, tast venligst et gyldigt input fra menuen.");
            }
        } while (userInput != 9);
    }

    private void treasurerMenu() {

        do {
            System.out.println("""
                    Kasserer Menu
                    1. Se Kontingentliste
                    2. Se samlet kontingetbeløb
                    9. Tilbage til hovedmenu""");

            userInput = getValidInput();

            switch (userInput) {
                case 1 -> ctrl.showSubscriptionList();
                case 2 -> {
                    System.out.println("Totale beløb: " + ctrl.getTotalSubscriptionAmount());
                }
                case 9 -> {
                    System.out.println("Tilbage til hovedmenuen.");
                    ctrl.saveMemberList();
                    run();
                }
                default -> System.out.println("Fejl, tast venligst et gyldigt input fra menuen.");
            }
        } while (userInput != 9);
    }

    private void chairmanMenu() {

        do {
            System.out.println("""
                    Formand Menu
                    1. Opret Medlemskab
                    2. Liste over medlemmer
                    9. Tilbage til hovedmenu""");

            userInput = getValidInput();

            switch (userInput) {
                case 1 -> createMembership();
                case 2 -> showList();
                case 9 -> {
                    System.out.println("Tilbage til hovedmenuen.");
                    ctrl.saveMemberList();
                    run();
                }
                default -> System.out.println("Fejl, tast venligst et gyldigt input fra menuen.");
            }
        } while (userInput != 9);
    }

    private void createMembership() {

        System.out.println("Indtast navn:");
        String name = input.nextLine();

        System.out.println("Indtast alder:");
        int age = input.nextInt();
        input.nextLine();
        System.out.println("Indtast mail:");
        String mail = input.nextLine();

        System.out.println("Aktivt Medlemskab: (true/false)");
        boolean activeMembership = Boolean.parseBoolean(input.nextLine());

        System.out.println("Indtast fødselsdagsdato:");
        LocalDate birthday = LocalDate.parse(input.nextLine(), formatter);

        System.out.println("TEST (Bliver sat automatisk efter oprettelse(Ændres måske))");
        LocalDate lastPayment = LocalDate.now();

        ctrl.addMember(name, age, mail, activeMembership, birthday, lastPayment);
    }

    private void showList() {
        System.out.println(ctrl.showList());
    }


    private int getValidInput() {
        int inputNumber = 0;
        try {
            inputNumber = input.nextInt();
            input.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Fejl, tast venligst et gyldigt input fra menuen.");
            input.nextLine();
        }
        return inputNumber;
    }

    private void displayMainMenu() {
        System.out.println("Velkommen til Delfin Svømmeklub\nTast venligst dit ønskede input");
        System.out.println("""
                1. Formand Menu
                2. Kasserer Menu
                3. Træner Menu
                9. Afslut""");
    }

    // TODO: lav det her lårt
    private void top5() {

    }

    // TODO: fejlhåndtering
    public void coachSearch() {

        String out = "";

        System.out.println("Indtast mail:");
        String mail = input.nextLine();

        for (Member member: ctrl.getMemberList()) {
            if (mail.equals(member.getMail())) {
                for (Result result: ctrl.getResultList()) {
                    if (mail.equals(result.getMail())) {
                        out += result.getDiscipline()
                                + "\n" + result.getTime()
                                + "\n" + result.getDate()
                                + "\n\n";
                    }
                }
                System.out.println(member.getName() + "\n" + out); //return member.getName() + "\n" + out;
            }
        }
        System.out.println("Intet medlem fundet.");
    }

    // TODO: fejlhåndtering, inputsikring
    private void update() {

        LocalDate date;
        String time;
        String discipline;

        Member chosenMember = null;
        System.out.println("Indtast email på det medlem som skal opdateres:");
        String mail = input.nextLine();
        for (Member member: ctrl.getMemberList()) {
            if (mail.equals(member.getMail())) {
                chosenMember = member;
            }
        }
        if (chosenMember == null) {
            System.out.println("Medlem ikke fundet");
        }

        System.out.println("Indtast dato:");
        date = LocalDate.parse(input.nextLine(), formatter);
        System.out.println("Indtast tid (format mm:ss.ms):");
        time = input.nextLine();
        System.out.println("Indtast disciplin:");
        discipline = input.nextLine();

        ctrl.addResult(mail, date, time, discipline);
        ctrl.saveResultList();
    }
}
