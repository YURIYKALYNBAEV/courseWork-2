package dailyplanner;

import exception.IncorrectArgumentException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WeeklyTask extends Task {
    public WeeklyTask(String title,
                      LocalDateTime dateTime,
                      String description,
                      Type type) throws IncorrectArgumentException {
        super(title, dateTime, description, type);
    }

    @Override
    public boolean appearsIn(LocalDate localDate) {
        return ((this.getDateTime().toLocalDate().isBefore(localDate) ||
                this.getDateTime().toLocalDate().isEqual(localDate)) &&
                this.getDateTime().toLocalDate().getDayOfWeek().equals(localDate.getDayOfWeek()));
    }
}
