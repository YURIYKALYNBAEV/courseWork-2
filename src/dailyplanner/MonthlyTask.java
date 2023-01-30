package dailyplanner;

import exception.IncorrectArgumentException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MonthlyTask extends Task {
    public MonthlyTask(String title,
                       LocalDateTime dateTime,
                       String description,
                       Type type,
                       Repeatability repeatability) throws IncorrectArgumentException {
        super(title, dateTime, description, type, repeatability);
    }

    @Override
    public boolean appearsIn(LocalDate localDate) {
        return false;
    }


    @Override
    public LocalDateTime getTaskNextTime(LocalDateTime dateTime) {
        return dateTime.plusMonths(1);
    }
}
