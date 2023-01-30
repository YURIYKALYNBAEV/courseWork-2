import dailyplanner.*;
import exception.IncorrectArgumentException;
import exception.TaskNotFoundException;
import service.TaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IncorrectArgumentException, TaskNotFoundException {
        task();
    }

    public static void task() throws IncorrectArgumentException, TaskNotFoundException {
        TaskService taskService = new TaskService();

        taskService.add(new OneTimeTask("Test1",
                LocalDateTime.now(),
                "Test1",
                Type.PERSONAL,
                Repeatability.ONE_TIME));
        taskService.add(new OneTimeTask("Test4",
                LocalDateTime.now(),
                "Test4",
                Type.PERSONAL,
                Repeatability.ONE_TIME));
        taskService.add(new OneTimeTask("Test5",
                LocalDateTime.now(),
                "Test5",
                Type.PERSONAL,
                Repeatability.ONE_TIME));
        taskService.add(new DailyTask("Test2",
                LocalDateTime.now().plusDays(1),
                "Test2",
                Type.WORK,
                Repeatability.DAILY));

        taskService.add(new MonthlyTask("Test3",
                LocalDateTime.now().plusMonths(1),
                "Test3",
                Type.WORK,
                Repeatability.MONTHLY));

        Task task = taskService.remove(5);
        System.out.println("Удалена задача по id " + task.getId() + " " + task);
        Task task3 = taskService.remove(2);
        System.out.println("Удалена задача по id " + task3.getId() + " " + task);

        List<Task> removedTasks = taskService.getRemovedTasks();
        System.out.println("Список удаленных задач: " + removedTasks);

        Collection<Task> taskCollection = taskService.getAllByDate(LocalDate.now());
        System.out.println("Задачи на день:");
        System.out.println(taskCollection);
        Task task1 = taskService.updateDescription(4, "Test8");
        System.out.println(task1);
        Task task2 = taskService.updateTitle(1, "Test10");
        System.out.println(task2);


    }
}