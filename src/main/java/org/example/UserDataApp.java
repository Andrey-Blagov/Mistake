package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

class InvalidDataFormatException extends Exception {
    public InvalidDataFormatException(String message) {
        super(message);
    }
}

public class UserDataApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        try {
            System.out.println("1. Ввести новые данные");
            System.out.println("2. Прочитать данные из файла");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            if (choice == 1) {
                System.out.println("Введите данные (Фамилия Имя Отчество дата_рождения номер_телефона пол):");
                String userInput = scanner.nextLine();

                try {
                    String[] userData = parseUserData(userInput);
                    saveToFile(userData);
                    System.out.println("Данные успешно сохранены в файл.");
                } catch (InvalidDataFormatException | IOException e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
            } else if (choice == 2) {
                System.out.println("Введите имя файла для чтения данных:");
                String fileName = scanner.nextLine();
                readFromFile(fileName);
            } else {
                System.err.println("Некорректный выбор.");
            }
        } finally {
            scanner.close();
        }
    }

    private static String[] parseUserData(String userInput) throws InvalidDataFormatException {
        String[] data = userInput.split(" ");

        if (data.length != 6) {
            throw new InvalidDataFormatException("Неверное количество данных. Ожидается 6 значений.");
        }

        try {
            String lastName = data[0];
            String firstName = data[1];
            String middleName = data[2];

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date birthDate = dateFormat.parse(data[3]);

            long phoneNumber = Long.parseLong(data[4]);

            char gender = data[5].charAt(0);
            if (gender != 'f' && gender != 'm') {
                throw new InvalidDataFormatException("Неверный формат пола. Используйте 'f' или 'm'.");
            }

            return new String[]{lastName, firstName, middleName, dateFormat.format(birthDate), String.valueOf(phoneNumber), String.valueOf(gender)};
        } catch (NumberFormatException | ParseException e) {
            throw new InvalidDataFormatException("Ошибка при разборе данных: " + e.getMessage());
        }
    }

    private static void saveToFile(String[] userData) throws IOException {
        String fileName = userData[0] + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.join(" ", userData));
            writer.newLine();
        }
    }

    private static void readFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Данные из файла: " + line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        }
    }
}
