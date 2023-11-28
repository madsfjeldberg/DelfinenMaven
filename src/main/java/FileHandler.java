import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

    private File file;

    public void saveMemberList(ArrayList<Member> list) {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (PrintStream output = new PrintStream(file)) {
            for (Member member : list) {
                String out;
                out = member.getName()
                        + "," + member.getAge()
                        + "," + member.getMail()
                        + "," + member.isActiveMembership()
                        + "," + formatDate(member.getBirthday())
                        + "," + formatDate(member.getLastPaymentDate())
                        + "," + formatDate(member.getNextPaymentDate());

                output.println(out);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveResultList(ArrayList<Result> list) {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (PrintStream output = new PrintStream(file)) {
            for (Result result: list) {
                String out;
                out = result.getMail()
                        + "," + result.getDate()
                        + "," + result.getTime()
                        + "," + result.getDiscipline();

                output.println(out);
            }
        } catch (FileNotFoundException e)  {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Result> loadResultList() {

        ArrayList<Result> resultList = new ArrayList<>();
        String mail;
        LocalDate date;
        String time;
        String discipline;

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {
                String[] resultValues = reader.nextLine().split(",");
                mail = trimString(resultValues[0]);
                date = parseDate(resultValues[1]);
                time = trimString(resultValues[2]);
                discipline = trimString(resultValues[3]);

                Result result = new Result(mail, date, time, discipline);
                resultList.add(result);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return resultList;
    }

    public ArrayList<Member> loadMemberList(String fileName) {
        File file = new File(fileName);
        ArrayList<Member> memberList = new ArrayList<>();
        String name;
        int age;
        String mail;
        boolean activeMembership;
        LocalDate birthday;
        LocalDate lastPayment;

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {
                String[] memberValues = reader.nextLine().split(",");
                name = trimString(memberValues[0]);
                age = parseTrim(memberValues[1]);
                mail = trimString(memberValues[2]);
                activeMembership = parseBoolean(memberValues[3]);
                birthday = parseDate(memberValues[4]);
                lastPayment = parseDate(memberValues[5]);

                Member member = new Member(name, age, mail, activeMembership, birthday, lastPayment, isPaid);
                memberList.add(member);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return memberList;
    }

    public ArrayList<TournamentMember> loadTournamentMemberList() {

        ArrayList<TournamentMember> memberList = new ArrayList<>();
        String name;
        int age;
        String mail;
        boolean activeMembership;
        LocalDate birthday;
        LocalDate lastPayment;
        String time;
        LocalDate date;
        String discipline;
        String team;

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {
                String[] memberValues = reader.nextLine().split(",");
                name = trimString(memberValues[0]);
                age = parseTrim(memberValues[1]);
                mail = trimString(memberValues[2]);
                activeMembership = parseBoolean(memberValues[3]);
                birthday = parseDate(memberValues[4]);
                lastPayment = parseDate(memberValues[5]);
                date = parseDate(memberValues[6]);
                time = trimString(memberValues[7]);
                discipline = trimString(memberValues[8]);
                team = trimString(memberValues[9]);

                Result result = new Result(mail, date, time, discipline);
                TournamentMember member = new TournamentMember(name, age, mail, activeMembership, birthday, lastPayment, result, team);
                memberList.add(member);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Fil ikke fundet");
        }
        return memberList;
    }

    private String trimString(String input) {
        return input.trim();
    }

    private int parseTrim(String input) {
        return Integer.parseInt(trimString(input));
    }

    private static String formatDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return localDate.format(formatter);
    }

    private static LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    private static boolean parseBoolean(String string) {
        return string.equalsIgnoreCase("true");
    }

}