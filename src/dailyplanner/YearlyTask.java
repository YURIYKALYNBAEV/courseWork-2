package dailyplanner;

import exception.IncorrectArgumentException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class YearlyTask extends Task {
    public YearlyTask(String title,
                      LocalDateTime dateTime,
                      String description,
                      Type type, Repeatability repeatability) throws IncorrectArgumentException {
        super(title, dateTime, description, type, repeatability);
    }

    @Override
    public boolean appearsIn(LocalDate localDate) {
        return false;
    }


    @Override
    public LocalDateTime getTaskNextTime(LocalDateTime dateTime) {
        return dateTime.plusYears(1);
    }
}
