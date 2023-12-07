package userinterface;

import objects.CompResult;
import objects.Member;
import objects.Result;
import controller.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInterface{

    private final Controller ctrl;
    private final Scanner input;
    int userInput;
    DateTimeFormatter formatter;
    private final Pattern timePattern;
    private final Pattern mailPattern;
    private final LocalDate minDate = LocalDate.now().minusYears(100);
    private final LocalDate maxDate = LocalDate.now();

    public UserInterface() {
        ctrl = new Controller();
        input = new Scanner(System.in);
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        timePattern = Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$"); // sætter format til "12:34:56"
        mailPattern = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"); // sikrer mailformat
    }

    // funktion til at formatere tekst.
    public String format(String text, String type) {
        String ANSI_BOLD = "\u001B[1m";
        String ANSI_ITALIC = "\u001B[3m";
        String ANSI_UNDERLINE = "\u001B[4m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_RESET = "\u001B[0m";

        return switch (type.toUpperCase()) {
            case "BOLD" -> ANSI_BOLD + text + ANSI_RESET;
            case "ITALIC" -> ANSI_ITALIC + text + ANSI_RESET;
            case "UNDERLINE" -> ANSI_UNDERLINE + text + ANSI_RESET;
            case "RED" -> ANSI_RED + text + ANSI_RESET;
            case "GREEN" -> ANSI_GREEN + text + ANSI_RESET;
            case "BLUE" -> ANSI_BLUE + text + ANSI_RESET;
            default -> text;
        };
    }

    public void mainMenu() {

        String headline = format("Delfin Svømmeklub database.Database\n", "bold");
        String headline2 = format("Tast venligst dit ønskede input:\n", "italic");

        do {
            System.out.println(headline + headline2);
            System.out.println("""
                1. Formand Menu
                2. Kasserer Menu
                3. Træner Menu
                9. Afslut""");

            userInput = getValidInput();
            switch (userInput) {
                case 1 -> chairmanMenu();
                case 2 -> treasurerMenu();
                case 3 -> trainerMenu();
                case 9 -> System.out.println(format("Afslutter programmet.", "italic"));
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
                    4. Slet medlem
                    9. Tilbage til hovedmenu
                    """);

            userInput = getValidInput();

            switch (userInput) {
                case 1 -> createMember();
                case 2 -> showList();
                case 4 -> deleteMember();
                case 9 -> {
                    ctrl.saveMemberList();
                    mainMenu();
                }
            }
        } while (userInput != 9);
    }

    private void treasurerMenu() {

        do {
            System.out.println("""
                    Kasserer Menu
                    1. Se Medlemsliste
                    2. Se samlet kontingentbeløb
                    3. Se ubetalt kontingentliste
                    4. Se betalt kontingentliste
                    5. Opdater betalingsstatus for medlem
                    9. Tilbage til hovedmenu
                    """);

            userInput = getValidInput();
            switch (userInput) {
                case 1 -> System.out.println(ctrl.showSubscriptionList());
                case 2 -> System.out.println("Totale beløb: " + ctrl.getTotalSubscriptionAmount() + " kr. for indeværende år.\n");
                case 3 -> System.out.println(ctrl.showPayingMembers(false));
                case 4 -> System.out.println(ctrl.showPayingMembers(true));
                case 5 -> updatePaymentForMember();
                case 9 -> {
                    ctrl.saveMemberList();
                    mainMenu();
                }
            }
        } while (userInput != 9);
    }

    private void trainerMenu() {

        do {
            System.out.println("""
                    Træner Menu
                    1. Top 5 svømmere
                    2. Opdater resultat
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
                    mainMenu();
                }
            }
        } while (userInput != 9);
    }

    // FORMAND FUNKTIONER
    //--------------------------------------------------------------------------------------------------------------//

    public void createMember() {

        System.out.println(format("Indtast navn:", "italic"));
        String name = input.nextLine();
        String mail;
        LocalDate birthday = null;
        int phoneNumber;

        boolean validMail;
        do {
            System.out.println(format("Indtast mail:", "italic"));
            mail = input.nextLine();
            Matcher matcher = mailPattern.matcher(mail);
            validMail = matcher.matches();
            if (!validMail) {
                System.out.println(format("Ugyldigt format. prøv igen.", "italic"));
            }
        } while (!validMail);

        System.out.println(format("Aktivt Medlemskab: (y/n)", "italic"));
        boolean activeMembership = booleanCheck(input.nextLine());

        boolean validDate = false;
        do {
            System.out.println(format("Indtast fødselsdagsdato: (dd-MM-yyyy)", "italic"));
            try {
                birthday = LocalDate.parse(input.nextLine(), formatter);
                if (birthday.isAfter(minDate) && birthday.isBefore(maxDate)) {
                    validDate = true;
                } else
                    System.out.println(format("Dato skal være mellem " + formatter.format(minDate) + " og " + formatter.format(maxDate) + ".", "italic"));

            } catch (DateTimeParseException e) {
                System.out.println(format("Ugyldigt format. Prøv igen. (dd-MM-yyyy)", "italic"));
            }
        } while (!validDate);

        while (true) {
            System.out.println(format("Indtast telefonnummer: (eks. 12345678)", "italic"));

            if (input.hasNextInt()) {
                phoneNumber = input.nextInt();
                break;
            } else {
                System.out.println(format("Telefonnummer skal være et tal. Prøv igen.", "italic"));
                input.next();
            }
        }

        System.out.printf("Note: Medlemskabsstart bliver automatisk sat til nuværende dag: %s", LocalDate.now());
        LocalDate lastPayment = LocalDate.now();

        ctrl.addMember(name, mail, activeMembership, birthday, lastPayment, phoneNumber);
        System.out.println("Medlem oprettet.\n");
    }

    public void deleteMember() {
        System.out.println("Indtast email på det medlem, du ønsker at slette:");
        Iterator<Member> iterator = ctrl.getMemberList().iterator();
        String emailToDelete = input.nextLine();
        while (iterator.hasNext()) {
            Member member = iterator.next();
            if (member.getMail().equalsIgnoreCase(emailToDelete)) {
                iterator.remove();
                System.out.println("Medlem slettet: " + member.getName());
                return;
            }
        }
        System.out.println("Medlem ikke fundet med email: " + emailToDelete);
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

    //KASSERER FUNKTIONER
    //--------------------------------------------------------------------------------------------------------------//

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
                    // ctrl.saveMemberList();
                    System.out.println("Betaling opdateret for medlem: " + member.getName());
                } else {
                    System.out.println("Betaling ikke opdateret.");
                }
            }
        }
    }

    //TRÆNER FUNKTIONER
    //--------------------------------------------------------------------------------------------------------------//

    // viser en liste af de 5 hurtigste tider i hver disciplin, i både stævne og træning.
    public void top5() {
        System.out.println("""
        1. Turneringstider
        2. Træningstider
        9. Tilbage til Træner Menu
        """);

        int timeTypeOption = getValidInput();
        boolean isCompetition = timeTypeOption == 1;
        if (timeTypeOption == 9) {
            trainerMenu();
        }

        System.out.println("""
        1. Senior
        2. Junior
        9. Tilbage til Træner Menu
        """);

        int categoryOption = getValidInput();
        boolean isSenior = categoryOption == 1;
        if (categoryOption == 9) {
            trainerMenu();
        }
        String top5Result = ctrl.showTop5(isCompetition, isSenior);
        System.out.println(top5Result + "\n");
    }

    // søger efter et enkelt medlem, og viser deres tider i træning og stævne.
    public void coachSearch() {
        boolean foundMember = false;
        boolean foundResult = false;

        System.out.println("Indtast navn eller mail:");
        String response = input.nextLine();
        String mail;

        for (Member member : ctrl.getMemberList()) {
            if (response.equalsIgnoreCase(member.getName()) || response.equalsIgnoreCase(member.getMail())) {
                foundMember = true;
                mail = member.getMail();
                StringBuilder outputTraining = new StringBuilder();
                StringBuilder outputCompetition = new StringBuilder();

                for (Result result : ctrl.getResultList()) {
                    if (mail.equals(result.getMail())) {
                        foundResult = true;
                        // sorterer og sætter format efter træning og turneringstid
                        if (result instanceof CompResult) {
                            outputCompetition.append(String.format("| %-15s | %-10s | %-10s | %-20s | %-10s | %n",
                                    result.getDiscipline(), result.getTime(), result.getDate(),
                                    ((CompResult) result).getCompetition(), ((CompResult) result).getPlacement()));
                        } else {
                            outputTraining.append(String.format("| %-15s | %-10s | %-10s | %-20s | %-10s | %n",
                                    result.getDiscipline(), result.getTime(), result.getDate(), "Træning", "n/a"));
                        }
                    }
                }
                System.out.printf("\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s",
                        member.getName(), "─".repeat(82), "| Disciplin       | Tid        | Dato       | Stævne               | Placering  |",
                        "─".repeat(82), outputCompetition,"─".repeat(82), outputTraining, "─".repeat(82));
                System.out.println();
            }
        }

        if (!foundMember) {
            System.out.println("\u001B[31mIntet medlem fundet.\u001B[0m");
        }
        if (!foundResult) {
            System.out.println("\u001B[31mIngen tid registreret.\u001B[0m");
        }
    }

    // tilføjer en tid til et medlem.
    private void update() {

        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SS");
        Member chosenMember = null;
        LocalDate date = null;
        String time;
        String discipline;
        String competition = "";
        String placement = "";

        System.out.println("Stævnetid? (y/n): ");
        boolean comp = booleanCheck(input.nextLine());
        if (comp) {
            System.out.println("Indtast stævnenavn: ");
            competition = input.nextLine();
            System.out.println("Indtast placering: ");
            placement = input.nextLine();
        }

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
                    System.out.println("Indtast dato: (dd-MM-yyyy)");
                    date = LocalDate.parse(input.nextLine(), formatter);
                    if (date.isAfter(minDate) && date.isBefore(maxDate)) {
                        validDate = true;
                    } else System.out.printf("Dato skal være mellem %s og %s.\n",
                            formatter.format(minDate), formatter.format(maxDate));
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
            String[] disciplines = {"crawl","rygcrawl","bryst","butterfly"};
            do {
                System.out.println("Indtast disciplin:");
                discipline = input.nextLine().toLowerCase();
                for(String valid : disciplines) {
                    if (valid.equalsIgnoreCase(discipline)) {
                        validDiscipline = true;
                        break;
                    }
                }
                if (!validDiscipline) System.out.println("Ugyldig disciplin. (crawl, rygcrawl, bryst, butterfly)");
            } while (!validDiscipline);

            if (comp) {
                ctrl.addCompResult(mail, date, time, discipline, placement, competition);
            } else {
                for (Result result: ctrl.getResultList()) {
                    if (discipline.equals(result.getDiscipline())) {
                        // Checker om tiden er hurtigere eller langsommere. Hvis langsommere, gemmes ikke, hvis hurtigere, erstatter gemt tid.
                        try {
                            Date time1 = sdf.parse(time);
                            Date time2 = sdf.parse(result.getTime());
                            int compare = time1.compareTo(time2);
                            if (compare < 0) {
                                ctrl.getResultList().remove(result);
                                ctrl.addResult(mail, date, time, discipline);
                            }
                            if (compare > 0) {
                                System.out.println("Tid er langsommere, gemmes ikke.");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                }
            }
            System.out.println("Tid opdateret.");
        }

        // inputchecker for boolean værdier.
    private boolean booleanCheck(String response) {
        while (true) {
            if (response.equalsIgnoreCase("y")) {
                return true;
            } else if (response.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Indtast 'y' eller 'n'");
            response = input.nextLine();
        }
    }
}
