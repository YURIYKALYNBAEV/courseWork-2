package dailyplanner;

import exception.IncorrectArgumentException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Task {
    private static int idGenerator = 1;
    private String title;
    private final int id;
    private LocalDateTime dateTime;
    private String description;
    private Type type;

    public Task(String title,
                LocalDateTime dateTime,
                String description,
                Type type) throws IncorrectArgumentException {
        setTitle(title);
        setType(type);
        setDescription(description);
        setDateTime(dateTime);
        this.id = idGenerator++;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws IncorrectArgumentException {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        } else {
            throw new IncorrectArgumentException("заголовок задачи");
        }
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) throws IncorrectArgumentException {
        if (type != null) {
            this.type = type;
        } else {
            throw new IncorrectArgumentException("тип задачи");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws IncorrectArgumentException {
        if (description != null && !description.isEmpty()) {
            this.description = description;
        } else {
            throw new IncorrectArgumentException("описание задачи");
        }
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) throws IncorrectArgumentException {
        if (dateTime != null) {
            this.dateTime = dateTime;
        } else {
            throw new IncorrectArgumentException("дата и время задачи");
        }
    }

    public abstract boolean appearsIn(LocalDate localDate);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(dateTime, task.dateTime) && Objects.equals(description, task.description) && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, id, dateTime, description, type);
    }

    @Override
    public String toString() {
        return "Задача: " +
                "заголовок " + title +
                ", идентификатор " + id +
                ", дата и время " + dateTime +
                ", описание " + description + '\'' +
                ", тип " + type;
    }
}
