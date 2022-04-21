package tomberg.fun.spring.air_flights.entity;

import org.springframework.lang.Nullable;
import tomberg.fun.spring.air_flights.entity.time.Day;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;

@Entity
@Table(name = "regulators")
public class Regulator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "day_id")
    private Day day;

    @Nullable
    private LocalDate validFromDate;

    @Nullable
    private LocalDate validToDate;

    @Nullable
    private Time departureTime;

    @Nullable
    private Time arriveTime;

    public Regulator() {
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Nullable
    public LocalDate getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(@Nullable LocalDate validFromDate) {
        this.validFromDate = validFromDate;
    }

    @Nullable
    public LocalDate getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(@Nullable LocalDate validToDate) {
        this.validToDate = validToDate;
    }

    @Nullable
    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(@Nullable Time departureTime) {
        this.departureTime = departureTime;
    }

    @Nullable
    public Time getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(@Nullable Time arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}