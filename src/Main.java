import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные в формате: Фамилия Имя Отчество Дата_рождения Номер_телефона Пол");
        String input = scanner.nextLine();  // принимаем через пробел данные
        String[] data = input.split(" "); // создаем массив из строки, которую ввели
        try {                                   // после пробела создается следующий элемент массива
            if (data.length != 6) {
                throw new IllegalArgumentException("Введено некорректное количество данных!");
            }
            String lastName = data[0];
            String name = data[1];
            String patronymic = data[2];
            String birthDateStr = data[3];
            String phoneNumber = data[4];
            char gender = data[5].charAt(0);

            String onlyLetters = lastName.replaceAll("[^a-zA-Zа-яА-Я]", ""); // следующие три условия проверяют, что строки имя фамилия
            if (!onlyLetters.equals(lastName)){                                               // и отчества состоят только из русских букв
                throw new IllegalArgumentException("Фамилия введена некорректно!");
            }
            onlyLetters = name.replaceAll("[^a-zA-Zа-яА-Я]", "");
            if (!onlyLetters.equals(name)){
                throw new IllegalArgumentException("Имя введено некорректно!");
            }
            onlyLetters = patronymic.replaceAll("[^a-zA-Zа-яА-Я]", "");
            if (!onlyLetters.equals(patronymic)){
                throw new IllegalArgumentException("Отчество введено некорректно!");
            }

            if (phoneNumber.length() != 11){
                throw new IllegalArgumentException("Некорректный номер телефона!");
            }

            if (gender != 'f' && gender != 'm') {
                throw new IllegalArgumentException("Некорректное значение пола");
            }

            // Проверяем, что введенная дата может существовать
            String formattedDate;
            if (birthDateStr.length() == 8) {
                DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
                dateFormat.setLenient(false);
                Date date = dateFormat.parse(birthDateStr);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1; // Месяцы в Calendar начинаются с 0
                int year = calendar.get(Calendar.YEAR);
                DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
                formattedDate = outputFormat.format(date);
            }
            else {
                throw new IllegalArgumentException("Введена некорректная дата!");
            }

            // ниже создаем строку со всеми парсированными данными. Потом эту строку передадим в метод для записи в файл
            boolean isDigitsOnly = Pattern.matches("\\d+", phoneNumber);
            if (isDigitsOnly) {
                long phoneNumberParse = Long.parseLong(phoneNumber);
            } else {
                throw new IllegalArgumentException("Некорректный номер телефона!");
            }

            String formattedData = lastName + ", " + name + ", " + patronymic + ", " + formattedDate + ", " + phoneNumber + ", " + gender;
            writeToFile(lastName, formattedData);
            System.out.println("Данные успешно записаны в файл");
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        catch (IOException e){
            System.out.println("Ошибка при записи в файл:");
            e.printStackTrace();
        }
        catch (ParseException e){
            System.out.println("Некорректная дата! Введите дату формата dd.mm.yyyy.");
        }
    }
    private static void writeToFile(String lastName, String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(lastName + ".txt", true));
        writer.write(data);
        writer.newLine();
        writer.close();
    }
}