package tomberg.fun.spring.air_flights.entity.time;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DateSelector {
    public List<LocalDate> getDatesByWeekdayAndBetweenTwoDays(int day_num, LocalDate from, LocalDate to) {
        List<LocalDate> list = from.datesUntil(to).collect(Collectors.toList());
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate date : list) {
            if (date.getDayOfWeek().getValue() == day_num) {
                dates.add(date);
            }
        }
        return dates;
    }
}
