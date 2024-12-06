package dz3;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class MainRepository {

    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "------";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            UserRepository userRepository = new UsersRepositoryJdbcImpl(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Выберите действие:");
                System.out.println("1. Добавить одного пользователя");
                System.out.println("2. Добавить нескольких пользователей");
                System.out.println("3. Показать всех пользователей");
                System.out.println("4. Найти пользователя по ID");
                System.out.println("5. Найти пользователя по email");
                System.out.println("6. Найти пользователя по номеру телефона");
                System.out.println("7. Найти пользователя по опыту работы");
                System.out.println("8. Найти пользователя по возрасту");
                System.out.println("9. Удалить пользователя по ID");
                System.out.println("10. Удалить последнего пользователя");
                System.out.println("11. Обновить данные пользователя");
                System.out.println("12. Выйти");
                System.out.print("Ваш выбор: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addUser(scanner, userRepository);
                        break;
                    case 2:
                        addMultipleUsers(scanner, userRepository);
                        break;
                    case 3:
                        showAllUsers(userRepository);
                        break;
                    case 4:
                        findUserById(scanner, userRepository);
                        break;
                    case 5:
                        findUserByEmail(scanner, userRepository);
                        break;
                    case 6:
                        findUserByPhoneNumber(scanner, userRepository);
                        break;
                    case 7:
                        findUserByExperience(scanner, userRepository);
                        break;
                    case 8:
                        findUserByAge(scanner, userRepository);
                        break;
                    case 9:
                        removeUserById(scanner, userRepository);
                        break;
                    case 10:
                        removeLastUser(userRepository);
                        break;
                    case 11:
                        updateUser(scanner, userRepository);
                        break;
                    case 12:
                        System.out.println("Выход из программы...");
                        return;
                    default:
                        System.out.println("Неверный выбор, попробуйте снова.");
                }
            }
        }
    }

    private static void addUser(Scanner scanner, UserRepository userRepository) {
        System.out.println("Введите данные для пользователя:");

        String firstName = getValidName(scanner, "Имя: ");
        String lastName = getValidName(scanner, "Фамилия: ");

        int age = getValidAge(scanner);

        System.out.print("Email: ");
        String email = scanner.nextLine();

        String phoneNumber = getValidPhoneNumber(scanner);

        int yearsExperience = getValidExperience(scanner);

        User user = new User(null, firstName, lastName, age, email, phoneNumber, yearsExperience);
        userRepository.save(user);

        System.out.println("Пользователь добавлен!");
    }

    private static String getValidName(Scanner scanner, String prompt) {
        String name;
        while (true) {
            System.out.print(prompt);
            name = scanner.nextLine();
            if (name.matches("^[a-zA-Zа-яА-Я]+$")) {
                break;
            } else {
                System.out.println("Неверный формат. Имя и фамилия должны содержать только буквы.");
            }
        }
        return name;
    }

    private static int getValidAge(Scanner scanner) {
        int age;
        while (true) {
            System.out.print("Возраст: ");
            try {
                age = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Пожалуйста, введите целое число для возраста.");
            }
        }
        return age;
    }

    private static String getValidPhoneNumber(Scanner scanner) {
        String phoneNumber;
        while (true) {
            System.out.print("Телефон: ");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.matches("^\\+?\\d+$")) {
                break;
            } else {
                System.out.println("Неверный формат телефона. Номер должен содержать только цифры и символ '+'.");
            }
        }
        return phoneNumber;
    }

    private static int getValidExperience(Scanner scanner) {
        int yearsExperience;
        while (true) {
            System.out.print("Опыт работы (в годах): ");
            try {
                yearsExperience = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Пожалуйста, введите целое число для опыта работы.");
            }
        }
        return yearsExperience;
    }

    private static void addMultipleUsers(Scanner scanner, UserRepository userRepository) {
        System.out.print("Введите количество пользователей для добавления: ");
        int count = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < count; i++) {
            addUser(scanner, userRepository);
        }
    }

    private static void showAllUsers(UserRepository userRepository) {
        userRepository.findAll().forEach(user ->
                System.out.println(user.getId() + " " + user.getFirstName() + " " + user.getLastName() + " " + user.getAge() + " " + user.getEmail() + " " + user.getPhoneNumber() + " " + user.getYearsExperience())
        );
    }

    private static void findUserById(Scanner scanner, UserRepository userRepository) {
        System.out.print("Введите ID пользователя: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        userRepository.findById(id).ifPresentOrElse(
                user -> System.out.println(user.getId() + " " + user.getFirstName() + " " + user.getLastName() + " " + user.getAge() + " " + user.getEmail() + " " + user.getPhoneNumber() + " " + user.getYearsExperience()),
                () -> System.out.println("Пользователь не найден")
        );
    }

    private static void findUserByEmail(Scanner scanner, UserRepository userRepository) {
        System.out.print("Введите email пользователя: ");
        String email = scanner.nextLine();

        List<User> users = userRepository.findAllByEmail(email);
        if (users.isEmpty()) {
            System.out.println("Пользователь с таким email не найден.");
        } else {
            users.forEach(user ->
                    System.out.println(user.getId() + " " + user.getFirstName() + " " + user.getLastName() + " " + user.getAge() + " " + user.getEmail() + " " + user.getPhoneNumber() + " " + user.getYearsExperience())
            );
        }
    }

    private static void findUserByPhoneNumber(Scanner scanner, UserRepository userRepository) {
        System.out.print("Введите номер телефона пользователя: ");
        String phoneNumber = scanner.nextLine();

        List<User> users = userRepository.findAllByPhoneNumber(phoneNumber);
        if (users.isEmpty()) {
            System.out.println("Пользователь с таким номером телефона не найден.");
        } else {
            users.forEach(user ->
                    System.out.println(user.getId() + " " + user.getFirstName() + " " + user.getLastName() + " " + user.getAge() + " " + user.getEmail() + " " + user.getPhoneNumber() + " " + user.getYearsExperience())
            );
        }
    }

    private static void findUserByExperience(Scanner scanner, UserRepository userRepository) {
        System.out.print("Введите минимальный опыт работы (в годах): ");
        int yearsExperience = scanner.nextInt();
        scanner.nextLine();

        List<User> users = userRepository.findAllByYearsExperience(yearsExperience);
        if (users.isEmpty()) {
            System.out.println("Пользователей с таким опытом работы не найдено.");
        } else {
            users.forEach(user ->
                    System.out.println(user.getId() + " " + user.getFirstName() + " " + user.getLastName() + " " + user.getAge() + " " + user.getEmail() + " " + user.getPhoneNumber() + " " + user.getYearsExperience())
            );
        }
    }

    private static void removeUserById(Scanner scanner, UserRepository userRepository) {
        System.out.print("Введите ID пользователя для удаления: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        userRepository.removeById(id);
        System.out.println("Пользователь удален!");
    }

    private static void findUserByAge(Scanner scanner, UserRepository userRepository) {
        System.out.print("Введите возраст для поиска пользователя: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        List<User> users = userRepository.findAllByAge(age);
        if (users.isEmpty()) {
            System.out.println("Пользователя с таким возрастом не найдено.");
        } else {
            users.forEach(user ->
                    System.out.println(user.getId() + " " + user.getFirstName() + " " + user.getLastName() + " " + user.getAge() + " " + user.getEmail() + " " + user.getPhoneNumber() + " " + user.getYearsExperience())
            );
        }
    }

    private static void removeLastUser(UserRepository userRepository) {
        userRepository.remove(null);
        System.out.println("Последний пользователь удален!");
    }

    private static void updateUser(Scanner scanner, UserRepository userRepository) {
        System.out.print("Введите ID пользователя для обновления: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            System.out.println("Текущие данные пользователя:");
            System.out.println(user.getId() + " " + user.getFirstName() + " " + user.getLastName() + " " + user.getAge() + " " + user.getEmail() + " " + user.getPhoneNumber() + " " + user.getYearsExperience());
            System.out.println("Выберите данные, которые хотите изменить (оставьте пустым, если не хотите менять):");

            System.out.print("Имя (" + user.getFirstName() + "): ");
            String firstName = scanner.nextLine();
            if (firstName.isEmpty()) firstName = user.getFirstName();
            else firstName = getValidName(scanner, "Имя: ");

            System.out.print("Фамилия (" + user.getLastName() + "): ");
            String lastName = scanner.nextLine();
            if (lastName.isEmpty()) lastName = user.getLastName();
            else lastName = getValidName(scanner, "Фамилия: ");

            int age = user.getAge();
            System.out.print("Возраст (" + age + "): ");
            String ageInput = scanner.nextLine();
            if (!ageInput.isEmpty()) {
                try {
                    age = Integer.parseInt(ageInput);
                } catch (NumberFormatException e) {
                    System.out.println("Неверный ввод возраста. Оставлено старое значение.");
                }
            }

            String phoneNumber = user.getPhoneNumber();
            System.out.print("Телефон (" + phoneNumber + "): ");
            String phoneInput = scanner.nextLine();
            if (!phoneInput.isEmpty()) phoneNumber = phoneInput;

            int yearsExperience = user.getYearsExperience();
            System.out.print("Опыт работы (" + yearsExperience + "): ");
            String experienceInput = scanner.nextLine();
            if (!experienceInput.isEmpty()) {
                try {
                    yearsExperience = Integer.parseInt(experienceInput);
                } catch (NumberFormatException e) {
                    System.out.println("Неверный ввод опыта работы. Оставлено старое значение.");
                }
            }

            user = new User(id, firstName, lastName, age, user.getEmail(), phoneNumber, yearsExperience);
            userRepository.update(user);
            System.out.println("Данные пользователя обновлены!");
        } else {
            System.out.println("Пользователь не найден.");
        }
    }
}