import exception.IncorrectArgumentException;
import menu.Menu;
import service.TaskService;

public class Main {
    public static void main(String[] args) throws IncorrectArgumentException {
        TaskService taskService = new TaskService();
        Menu menu = new Menu(taskService);
        menu.startMenu();
    }
}