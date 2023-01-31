package menu;

import dailyplanner.*;
import exception.IncorrectArgumentException;
import exception.TaskNotFoundException;
import service.TaskService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Menu {
    private final TaskService taskService;
    private final Scanner console;
    private boolean isExit; // false - выходим из цикла
    private final Pattern DATE_TIME_PATTERN = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}\\:\\d{2}");
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final Pattern DATE_PATTERN = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public Menu(TaskService taskService) {
        this.taskService = taskService;
        this.console = new Scanner(System.in);
        this.isExit = true;
    }

    public void printMenu() {
        System.out.println("1. Добавить задачу\n" +
                "2. Удалить задачу\n" +
                "3. Получить задачи на день\n" +
                "0. Выход");
    }

    public void startMenu() {
        try {
            while (isExit) {
                String command;
                printMenu();
                System.out.println("Выберите пункт меню: ");
                if (console.hasNextInt()) {
                    int menuChoice = console.nextInt();
                    switch (menuChoice) {
                        case 0: // выход
                            isExit = false;
                            break;
                        case 1: // Добавить задачу
                            addTask(console);
                            break;
                        case 2: // Удалить задачу
                            deleteTask(console);
                            break;
                        case 3: // Получить задачи на день
                            printTasksByDay(console);
                            break;
                    }
                } else {
                    console.next();
                    System.out.println("Выберите пункт меню из списка!");
                }
            }
        } catch (IncorrectArgumentException e) {
            e.printStackTrace();
        }
    }

    private void addTask(Scanner console) throws IncorrectArgumentException {
        console.useDelimiter("\n");

        System.out.print("Введите название задачи: ");
        String title = console.next();
        if (title.isBlank()) {
            System.out.println("Небходимо ввести название задачи!");
            console.close();
        }

        System.out.print("Введите описание задачи: ");
        String description = console.next();
        if (description.isBlank()) {
            System.out.println("Небходимо ввести описание задачи!");
            console.close();
        }

        System.out.print("Введите тип задачи (1-личная, 2-рабочая): ");
        Type type = null;

        int taskTypeChoice = console.nextInt();
        if (taskTypeChoice == 1) {
            type = Type.PERSONAL;
        } else if (taskTypeChoice == 2) {
            type = Type.WORK;
        } else {
            System.out.println("Тип задачи введен некорректно!");
            console.close();
        }
        System.out.println("Введите дату и время задачи в формате dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = null;
        if (console.hasNext(DATE_TIME_PATTERN)) {
            String taskTime = console.next(DATE_TIME_PATTERN);
            dateTime = LocalDateTime.parse(taskTime, DATE_TIME_FORMATTER);
        } else {
            System.out.println("Введите дату и время задачи в формате dd.MM.yyyy HH:mm");
            console.close();
        }
        if (dateTime == null) {
            System.out.println("Введите дату и время задачи в формате dd.MM.yyyy HH:mm");
            console.close();
        }
        System.out.println("Введите повторяемость задачи (" +
                "1 - однократная," +
                "2 - ежедневная," +
                "3 - еженедельная," +
                "4 - ежемесячная," +
                "5 - ежегодная ): ");

        Task task = null;
        if (console.hasNextInt()) {
            int repeatability = console.nextInt();
            switch (repeatability) {
                case 1:
                    task = new OneTimeTask(title, dateTime, description, type);
                    break;
                case 2:
                    task = new DailyTask(title, dateTime, description, type);
                    break;
                case 3:
                    task = new WeeklyTask(title, dateTime, description, type);
                    break;
                case 4:
                    task = new MonthlyTask(title, dateTime, description, type);
                    break;
                case 5:
                    task = new YearlyTask(title, dateTime, description, type);
                    break;
                default:
                    System.out.println("Повторяемость задачи введена некорректно");
                    console.close();
            }
        }
        if (task != null) {
            taskService.add(task);
            System.out.println("Задача добавлена");
        } else {
            System.out.println("Введены некорректные данные по задаче!");
        }
    }

    private void deleteTask(Scanner console) {
        System.out.println("Введите id задачи для удаления: ");
        int id = console.nextInt();
        try {
            taskService.remove(id);
            System.out.println("Задача удалена");
        } catch (TaskNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printTasksByDay(Scanner console) {
        System.out.println("Введите дату задачи в формате dd.MM.yyyy: ");
        LocalDate localDate = null;
        if (console.hasNext(DATE_PATTERN)) {
            String taskDate = console.next(DATE_PATTERN);
            localDate = LocalDate.parse(taskDate, DATE_FORMATTER);

            Collection<Task> tasksByDay = taskService.getAllByDate(localDate);

            for (Task task : tasksByDay) {
                System.out.println(task);
            }
        } else {
            System.out.println("Введите дату задачи в формате dd.MM.yyyy: ");
            console.close();
        }
        if (localDate == null) {
            System.out.println("Введите дату задачи в формате dd.MM.yyyy: ");
            console.close();
        }

    }


}
