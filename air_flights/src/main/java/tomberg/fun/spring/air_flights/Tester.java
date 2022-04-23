package tomberg.fun.spring.air_flights;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Tester {
    public static List<LocalDate> getDatesBetweenUsingJava9(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate).collect(Collectors.toList());
    }
    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2022, 4, 1);
        LocalDate date1 = LocalDate.of(2022, 4, 30);
        List<LocalDate> dates = getDatesBetweenUsingJava9(date, date1);
        for (LocalDate date2 : dates) {
            if (date2.getDayOfWeek().getValue() == 1) {
                System.out.println(date2);
            }
        }
    }
}

