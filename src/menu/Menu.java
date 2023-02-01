package menu;

import dailyplanner.*;
import exception.IncorrectArgumentException;
import exception.TaskNotFoundException;
import service.TaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class Menu {
    private final TaskService taskService;
    private final Scanner console;
    private boolean isExit; // false - выходим из цикла
    private final Pattern dateTimePattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}\\:\\d{2}");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final Pattern datePattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public Menu(TaskService taskService) {
        this.taskService = taskService;
        this.console = new Scanner(System.in);
        this.isExit = true;
    }

    public void printMenu() {
        System.out.println(
                "1. Добавить задачу\n" +
                        "2. Удалить задачу по id\n" +
                        "3. Получить задачи на день\n" +
                        "4. Изменить заголовок задачи\n" +
                        "5. Изменить описание задачи\n" +
                        "6. Сгруппировать задачи по дате\n" +
                        "7. Вывести удаленные задачи\n" +
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
                        case 2: // Удалить задачу по id
                            deleteTask(console);
                            break;
                        case 3: // Получить задачи на день
                            printTasksByDay(console);
                            break;
                        case 4: // Изменить заголовок задачи
                            updateTitle(console);
                            break;
                        case 5: // Изменить описание задачи
                            updateDescription(console);
                            break;
                        case 6: // Сгруппировать задачи по дате
                            printAllGroupByDate();
                            break;
                        case 7: // Вывести удаленные задачи
                            printRemovedTasks();
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
        }

        System.out.print("Введите описание задачи: ");
        String description = console.next();
        if (description.isBlank()) {
            System.out.println("Небходимо ввести описание задачи!");
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
        }
        System.out.println("Введите дату и время задачи в формате dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = null;
        if (console.hasNext(dateTimePattern)) {
            String taskTime = console.next(dateTimePattern);
            dateTime = LocalDateTime.parse(taskTime, dateTimeFormatter);
        } else {
            System.out.println("Введите дату и время задачи в формате dd.MM.yyyy HH:mm");
        }
        if (dateTime == null) {
            System.out.println("Введите дату и время задачи в формате dd.MM.yyyy HH:mm");
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
            }
        }
        if (task != null) {
            taskService.add(task);
            System.out.println("Задача добавлена");
            System.out.println();
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
            System.out.println();
        } catch (TaskNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printTasksByDay(Scanner console) {
        System.out.println("Введите дату задачи в формате dd.MM.yyyy: ");
        LocalDate localDate = null;
        if (console.hasNext(datePattern)) {
            String taskDate = console.next(datePattern);
            localDate = LocalDate.parse(taskDate, dateFormatter);

            Collection<Task> tasksByDay = taskService.getAllByDate(localDate);

            for (Task task : tasksByDay) {
                System.out.println(task);
            }
        } else {
            System.out.println("Введите дату задачи в формате dd.MM.yyyy: ");
        }
        if (localDate == null) {
            System.out.println("Введите дату задачи в формате dd.MM.yyyy");
        }

    }

    private void updateTitle(Scanner console) {
        printAllGroupByDate();
        System.out.println("Введите id редактируемой задачи: ");
        int id = console.nextInt();
        try {
            System.out.println("Текущий заголовок: " +
                    taskService.getTaskById(id).getTitle());
            System.out.println("Введите новый заголовок: ");
            Scanner scanner = new Scanner(System.in);
            String title = scanner.next();
            Task task = taskService.updateTitle(id, title);
            System.out.println(task);
        } catch (TaskNotFoundException | IncorrectArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateDescription(Scanner console) {
        printAllGroupByDate();
        System.out.println("Введите id редактируемой задачи: ");
        int id = console.nextInt();
        try {
            System.out.println("Текущее описание: " +
                    taskService.getTaskById(id).getDescription());
            System.out.println("Введите новое описание: ");
            Scanner scanner = new Scanner(System.in);
            String description = scanner.next();
            Task task = taskService.updateDescription(id, description);
            System.out.println(task);
        } catch (TaskNotFoundException | IncorrectArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printAllGroupByDate() {
        Map<LocalDate, List<Task>> tasksAllGroupByDay = taskService.getAllGroupByDate();
        System.out.println(tasksAllGroupByDay);
    }

    private void printRemovedTasks() {
        List<Task> removedTasks = taskService.getRemovedTasks();
        for (Task removedTask : removedTasks
        ) {
            System.out.println(removedTask);
        }
    }
}
