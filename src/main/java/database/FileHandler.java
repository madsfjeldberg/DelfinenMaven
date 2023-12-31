package database;

import objects.CompResult;
import objects.Member;
import objects.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class FileHandler {

    public void saveMemberList(ArrayList<Member> list, String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Failed to create file: " + fileName);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (PrintStream output = new PrintStream(file)) {
            for (Member member : list) {
                String out;
                out = member.getName()
                        + "," + member.getMail()
                        + "," + member.isActiveMembership()
                        + "," + formatDate(member.getBirthday())
                        + "," + formatDate(member.getLastPaymentDate())
                        + "," + member.isPaid()
                        + "," + member.getPhoneNumber();

                output.println(out);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveResultList(ArrayList<Result> list, String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Failed to create file: " + fileName);
                }
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

                if (result instanceof CompResult) {
                    out += ","
                            + (((CompResult) result).getPlacement())
                            + ","
                            + ((CompResult) result).getCompetition();
                }
                output.println(out);
            }
        } catch (FileNotFoundException e)  {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Result> loadResultList(String fileName) {
        File file = new File(fileName);
        ArrayList<Result> resultList = new ArrayList<>();
        String mail;
        LocalDate date;
        String time;
        String discipline;
        String placement;
        String competition;

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {
                String[] resultValues = reader.nextLine().split(",");
                mail = trimString(resultValues[0]);
                date = parseDate(resultValues[1]);
                time = trimString(resultValues[2]);
                discipline = trimString(resultValues[3]);
                if (resultValues.length > 4) {
                    placement = trimString(resultValues[4]);
                    competition = trimString(resultValues[5]);
                    CompResult result = new CompResult(mail,date,time,discipline,placement,competition);
                    resultList.add(result);
                } else {
                    Result result = new Result(mail, date, time, discipline);
                    resultList.add(result);
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("Fil ikke fundet.");
        }
        return resultList;
    }

    public ArrayList<Member> loadMemberList(String fileName) {
        File file = new File(fileName);
        ArrayList<Member> memberList = new ArrayList<>();
        String name;
        String mail;
        boolean activeMembership;
        LocalDate birthday;
        LocalDate lastPayment;
        int phoneNumber;

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {
                String[] memberValues = reader.nextLine().split(",");
                name = trimString(memberValues[0]);
                mail = trimString(memberValues[1]);
                activeMembership = parseBoolean(memberValues[2]);
                birthday = parseDate(memberValues[3]);
                lastPayment = parseDate(memberValues[4]);
                phoneNumber = parseInt(memberValues[6]);

                Member member = new Member(name, mail, activeMembership, birthday, lastPayment, phoneNumber);
                memberList.add(member);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return memberList;
    }

    private String trimString(String input) {
        return input.trim();
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