package service;

import dailyplanner.Task;
import exception.IncorrectArgumentException;
import exception.TaskNotFoundException;

import java.time.LocalDate;
import java.util.*;

public class TaskService {
    private final Map<Integer, Task> taskMap = new TreeMap<>();
    private final List<Task> removedTasks = new LinkedList<>();

    public void add(Task task) {
        this.taskMap.put(task.getId(), task);
    }

    public Task updateTitle(int id, String title) throws TaskNotFoundException, IncorrectArgumentException {
        if (!taskMap.containsKey(id)) {
            throw new TaskNotFoundException(id);
        }
        taskMap.get(id).setDescription(title);
        return taskMap.get(id);
    }

    public Task updateDescription(int id, String description) throws TaskNotFoundException, IncorrectArgumentException {
        if (!taskMap.containsKey(id)) {
            throw new TaskNotFoundException(id);
        }
        taskMap.get(id).setDescription(description);
        return taskMap.get(id);
    }

    public List<Task> getRemovedTasks() {
        return removedTasks;
    }

    public Task remove(int id) throws TaskNotFoundException {
        if (!taskMap.containsKey(id)) {
            throw new TaskNotFoundException(id);
        }
        Task task = taskMap.get(id);
        removedTasks.add(task);
        taskMap.remove(id);
        return task;
    }

    public Collection<Task> getAllByDate(LocalDate localDate) {
        Collection<Task> collection = new ArrayList<>();
        for (Map.Entry<Integer, Task> entry : taskMap.entrySet()
        ) {
            if (entry.getValue().appearsIn(localDate)) {
                collection.add(entry.getValue());
            }
        }
        return collection;
    }

    public Map<LocalDate, List<Task>> getAllGroupByDate() {
        Map<LocalDate, List<Task>> resultMap = new TreeMap<>();
        for (Map.Entry<Integer, Task> entry : taskMap.entrySet()
        ) {
            LocalDate localDate = entry.getValue().getDateTime().toLocalDate();
            if (!resultMap.containsKey(localDate)) {
                resultMap.put(localDate, new LinkedList<>());
            }
            resultMap.get(localDate).add(entry.getValue());
        }
        return resultMap;
    }
}
