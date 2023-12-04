import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInterface {

    private final Controller ctrl;
    private final Scanner input;
    int userInput;
    DateTimeFormatter formatter;
    private final Pattern timePattern;
    private final Pattern mailPattern;
    LocalDate minDate = LocalDate.now().minusYears(100);
    LocalDate maxDate = LocalDate.now();

    public UserInterface() {
        ctrl = new Controller();
        input = new Scanner(System.in);
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        timePattern = Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$"); // sætter format til "12:34:56"
        mailPattern = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"); // sikrer mailformat
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
                    3. Søg efter svømmer
                    9. Tilbage til hovedmenu
                    """);

            userInput = getValidInput();

            switch (userInput) {
                case 1 -> top5();
                case 2 -> update();
                case 3 -> coachSearch();
                case 9 -> {
                    ctrl.saveMemberList();
                    ctrl.saveResultList();
                    run();
                }
            }
        } while (userInput != 9);
    }

    private void treasurerMenu() {

        do {
            System.out.println("""
                    Kasserer Menu
                    1. Se Kontingentliste
                    2. Se samlet kontingentbeløb
                    3. Se ubetalt kontingentliste
                    4. Se betalt kontingentliste
                    5. Opdater betalingsstatus for medlem
                    9. Tilbage til hovedmenu
                    """);

            userInput = getValidInput();

            switch (userInput) {
                case 1 -> System.out.println(ctrl.showSubscriptionList());
                case 2 -> System.out.println("Totale beløb: " + ctrl.getTotalSubscriptionAmount() + " pr. år.\n");
                case 3 -> System.out.println(ctrl.getUnpaidMember());
                case 4 -> System.out.println(ctrl.getPaidMember());
                case 5 -> updatePaymentForMember();
                case 9 -> {
                    ctrl.saveMemberList();
                    run();
                }
            }
        } while (userInput != 9);
    }

    private void chairmanMenu() {

        do {
            System.out.println("""
                    Formand Menu
                    1. Opret Medlemskab
                    2. Liste over medlemmer
                    3. Redigér medlem (IKKE IMPLEMENTERET)
                    9. Tilbage til hovedmenu
                    """);

            userInput = getValidInput();

            switch (userInput) {
                case 1 -> createMembership();
                case 2 -> showList();
                case 9 -> {
                    ctrl.saveMemberList();
                    run();
                }
            }
        } while (userInput != 9);
    }

    private void createMembership() {

        System.out.println("Indtast navn:");
        String name = input.nextLine();

        String mail;
        boolean validMail;
        do {
            System.out.println("Indtast mail:");
            mail = input.nextLine();
            Matcher matcher = mailPattern.matcher(mail);
            validMail = matcher.matches();
            if (!validMail) {
                System.out.println("Ugyldigt format. prøv igen.");
            }
        } while (!validMail);

        System.out.println("Aktivt Medlemskab: (y/n)");
        boolean run = true;
        boolean activeMembership = false;
        while (run) {
            String choice = input.nextLine();
            if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("n")) {
                if (choice.equalsIgnoreCase("y")) {
                    activeMembership = true;
                } else if (choice.equalsIgnoreCase("n")) {
                    activeMembership = false;
                }
                run = false;
            } else System.out.println(("Indtast 'y' eller 'n'"));
        }

        LocalDate birthday = null;
        boolean validDate = false;
        do {
            System.out.println("Indtast fødselsdagsdato (dd-MM-yyyy):");
            try {
                birthday = LocalDate.parse(input.nextLine(), formatter);
                if (birthday.isAfter(minDate) && birthday.isBefore(maxDate)) {
                    validDate = true;
                } else System.out.println("Dato skal være mellem " + formatter.format(minDate) + " og " + formatter.format(maxDate) + ".");

            } catch (DateTimeParseException e) {
                System.out.println("Ugyldigt format. Prøv igen. (dd-MM-yyyy)");
            }
        } while (!validDate);

        System.out.println("Note: Medlemskabsstart bliver automatisk sat til nuværende dag.");
        LocalDate lastPayment = LocalDate.now();

        ctrl.addMember(name, mail, activeMembership, birthday, lastPayment);
        System.out.println("Medlem oprettet.\n");
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

    private void top5() {
        do {
            System.out.println("""
                1. Sorter efter TOP 5 i crawl
                2. Sorter efter TOP 5 i rygcrawl
                3. Sorter efter TOP 5 i brystsvømning
                4. Sorter efter TOP 5 i butterfly
                9. Tilbage til trænermenu
                """);

            int swimStyleOption = getValidInput();

            switch (swimStyleOption) {
                case 1 -> ctrl.showTop5("crawl");
                case 2 -> ctrl.showTop5("rygcrawl");
                case 3 -> ctrl.showTop5("brystsvømning");
                case 4 -> ctrl.showTop5("butterfly");
                case 9 -> {
                    ctrl.saveMemberList();
                    trainerMenu();
                }
            }
        } while (userInput != 9);
    }

    public void coachSearch() {

        boolean foundMember = false;
        boolean foundResult = false;
        StringBuilder out = new StringBuilder();

        System.out.println("Indtast navn eller mail:");
        String response = input.nextLine();
        String mail;

        for (Member member: ctrl.getMemberList()) {
            if (response.equalsIgnoreCase(member.getName()) || response.equalsIgnoreCase(member.getMail())) {
                foundMember = true;
                mail = member.getMail();
                for (Result result: ctrl.getResultList()) {
                    if (mail.equals(result.getMail())) {
                        foundResult = true;
                        out.append("Disciplin: ").append(result.getDiscipline()).append("\n")
                                .append("Tid: ").append(result.getTime()).append("\n")
                                .append("Dato: ").append(result.getDate()).append("\n\n");
                    }
                }
                System.out.println(member.getName() + "\n" + out);
            }
        }
        if (!foundMember) {
            System.out.println("Intet medlem fundet.");
        }
        if (!foundResult) {
            System.out.println("Ingen tid registreret.");
        }
    }

    private void update() {

        Member chosenMember = null;
        LocalDate date = null;
        String time;
        String discipline;

        System.out.println("Indtast navn eller mail på det medlem der skal opdateres:");

        String mail = "";
        String response = input.nextLine();
        for (Member member: ctrl.getMemberList()) {
            if (response.equalsIgnoreCase(member.getName()) || response.equalsIgnoreCase(member.getMail())) {
                mail = member.getMail();
                chosenMember = member;
            }
        }

        if (chosenMember == null) {
            System.out.println("Medlem ikke fundet.");
        } else {
            boolean validDate = false;
            do {
                try {
                    System.out.println("Indtast dato (dd-MM-yyyy)");
                    date = LocalDate.parse(input.nextLine(), formatter);
                    if (date.isAfter(minDate) && date.isBefore(maxDate)) {
                        validDate = true;
                    } else System.out.println("Dato skal være mellem " + formatter.format(minDate) + " og " + formatter.format(maxDate) + ".");
                } catch (DateTimeParseException e) {
                    System.out.println("Ugyldigt format. Prøv igen.");
                }
            } while (!validDate);

            boolean validTime;
            do {
                System.out.println("Indtast tid (mm:ss:ms):");
                time = input.nextLine();
                Matcher matcher = timePattern.matcher(time);
                validTime = matcher.matches();
                if (!validTime) {
                    System.out.println("Ugyldigt format. Prøv igen.");
                }
            } while (!validTime);

            boolean validDiscipline = false;
            String[] disciplines = {"crawl","rygcrawl","brystsvømning","butterfly"};
            do {
                System.out.println("Indtast disciplin:");
                discipline = input.nextLine().toLowerCase();
                for(String valid : disciplines) {
                    if (valid.equalsIgnoreCase(discipline)) {
                        validDiscipline = true;
                        break;
                    }
                }
                if (!validDiscipline) System.out.println("Ugyldig disciplin. (crawl, rygcrawl, brystsvømning, butterfly)");
            } while (!validDiscipline);

            System.out.println("Tid opdateret.");
            ctrl.addResult(mail, date, time, discipline);
        }
    }

    private void updatePaymentForMember() {

        System.out.println("Indtast mail på det medlem, du skal opdatere betaling for:");
        String mail = input.nextLine();

        for (Member member : ctrl.getMemberList()) {
            if (mail.equals(member.getMail())) {
                System.out.println("Ønsker du at opdatere betalingen for dette medlem Ja eller Nej?\n" + "Navn: " + member.getName()
                        + ", Kontingent: " + member.calculateSubscription()
                        + ", Betalt: " + member.getIsPaidString());

                String response = input.nextLine().toLowerCase();
                if (response.equals("ja")) {
                    ctrl.updatePaymentForMember(mail);
                    member.updateLastPaymentDate();
                    ctrl.saveMemberList();
                    System.out.println("Betaling opdateret for medlem: " + member.getName());
                } else {
                    System.out.println("Betaling ikke opdateret.");
                }
            }
        }
    }
}
