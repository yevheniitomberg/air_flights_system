package tomberg.fun.spring.air_flights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tomberg.fun.spring.air_flights.entity.*;
import tomberg.fun.spring.air_flights.entity.location.Airport;
import tomberg.fun.spring.air_flights.entity.time.DateSelector;
import tomberg.fun.spring.air_flights.entity.time.Day;
import tomberg.fun.spring.air_flights.repository.*;
import tomberg.fun.spring.air_flights.service.RegulatorService;
import tomberg.fun.spring.air_flights.service.UserService;

import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AirportRepository airportRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    DayRepository dayRepository;

    @Autowired
    RegulatorRepository regulatorRepository;

    @Autowired
    DateSelector dateSelector;

    @Autowired
    PlaneRepository planeRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    RegulatorService regulatorService;

    @Autowired
    SelfFlightRepository selfFlightRepository;

    @GetMapping("")
    public String adminView(Model model) {
        model.addAttribute("user", userRepository.findByEmail(userService.getCurrentEmail()));
        return "admin_panel";
    }

    @PostMapping("")
    public String actionContributor(@RequestParam("submit") String action, Model model) {
        if (action.equals("add_air")) {
            return "redirect:/admin/add_airport";
        }
        if (action.equals("mk_con")) {
            return "redirect:/admin/make_connections";
        }
        if (action.equals("routes")) {
            return "redirect:/admin/routes";
        }
        return null;
    }

    @GetMapping("/add_airport")
    public String addAirportView(Model model) {
        model.addAttribute("airport", new Airport());
        return "add_airport";
    }

    @PostMapping("/add_airport")
    public String addAirportSaving(@ModelAttribute("airport") Airport airport, Model model) {
        Set<Airport> set = new HashSet<>();
        set.add(airportRepository.findByAirportCode("000"));
        airport.setConnected_airports(set);
        airportRepository.save(airport);
        return "redirect:/admin";
    }

    @GetMapping("/make_connections")
    public String makeConnections(Model model) {
        Airport airport = airportRepository.findByAirportCode("000");
        List<Airport> airportList = airportRepository.findAll();
        airportList.remove(airport);
        model.addAttribute("airports", airportList);
        return "make_connections";
    }

    @RequestMapping(path = "/make_connections", params = {"selected_airport", "action", "connections"}, method = RequestMethod.POST)
    public String actionsWithConnections(@RequestParam("selected_airport") String selected_airport,
                                         @RequestParam("action") String action,
                                         @RequestParam("connections") List<String> list, Model model) {

        if (action.equals("mk_con")) {
            if (list.isEmpty() || list.get(0).equals("000")) {
                return "redirect:/admin/make_connections";
            } else {
                List<Airport> list_obj = new ArrayList<>();
                for (String code : list) {
                    list_obj.add(airportRepository.findByAirportCode(code));
                }
                Airport airport = airportRepository.findByAirportCode(selected_airport);
                Set<Airport> set = airport.getConnected_airports();
                for (Airport air : list_obj) {
                    Route route = new Route();
                    route.setAirportFrom(airport);
                    route.setAirportTo(air);
                    routeRepository.save(route);
                    Route route1 = new Route();
                    route1.setAirportFrom(air);
                    route1.setAirportTo(airport);
                    routeRepository.save(route1);
                    Schedule schedule = new Schedule();
                    schedule.setRoute(route);
                    scheduleRepository.save(schedule);
                    Schedule schedule1 = new Schedule();
                    schedule1.setRoute(route1);
                    scheduleRepository.save(schedule1);
                    air.getConnected_airports().add(airport);
                    airportRepository.save(air);
                    set.add(air);
                }
                airportRepository.save(airport);
                return "redirect:/admin/make_connections";
            }
        }
        return "redirect:/admin/make_connections";
    }

    @RequestMapping(path = "/make_connections", params = {"sel_airport", "del_airport"}, method = RequestMethod.POST)
    @Transactional
    public String actionsWithConnections(@RequestParam("sel_airport") String sel_airport,
                                         @RequestParam("del_airport")String del_airport) {

        System.out.println(sel_airport);
        System.out.println(del_airport);
        Airport airport_sel = airportRepository.findByAirportCode(sel_airport);
        Airport airport_del = airportRepository.findByAirportCode(del_airport);

        Route route = routeRepository.findByAirportFromIdAndAirportToId(airport_sel.getId(), airport_del.getId());
        Route route1 = routeRepository.findByAirportFromIdAndAirportToId(airport_del.getId(), airport_sel.getId());

        Schedule schedule = scheduleRepository.findByRoute(route);
        Schedule schedule1 = scheduleRepository.findByRoute(route1);

        Regulator regulator = regulatorRepository.findBySchedule(schedule);
        Regulator regulator1 = regulatorRepository.findBySchedule(schedule1);

        List<Flight> flights = flightRepository.findAllByRegulator(regulator);
        List<Flight> flights1 = flightRepository.findAllByRegulator(regulator1);

        for (Flight flight : flights) {
            placeRepository.deleteAll(flight.getPlaces());
            flightRepository.delete(flight);
        }
        for (Flight flight : flights1) {
            placeRepository.deleteAll(flight.getPlaces());
            flightRepository.delete(flight);
        }

        regulatorRepository.deleteAllBySchedule(schedule);
        regulatorRepository.deleteAllBySchedule(schedule1);

        scheduleRepository.delete(schedule);
        scheduleRepository.delete(schedule1);

        routeRepository.delete(route1);
        routeRepository.delete(route);

        airport_sel.getConnected_airports().remove(airport_del);
        airport_del.getConnected_airports().remove(airport_sel);
        airportRepository.save(airport_sel);
        airportRepository.save(airport_del);
        return "redirect:/admin/make_connections";
    }


    @GetMapping("/routes")
    public String routesView(Model model) {
        List<Route> routes = routeRepository.findAllWithoutDefault();
        List<Schedule> schedules = new ArrayList<>();
        List<Day> days = dayRepository.findAll();
        for(Route route: routes) {
            schedules.add(scheduleRepository.findByRoute(route));
        }
        model.addAttribute("schedules", schedules);
        model.addAttribute("days", days);
        return "routes";
    }

    @PostMapping(value = "/routes", params = {"day", "schedule"})
    @Transactional
    public String routesDaysManaging(Model model, @RequestParam("day") List<Integer> days, @RequestParam("schedule") int id) {
        Schedule schedule = scheduleRepository.findById(id).get();

        Set<Day> set = schedule.getPeriodical();
        for (Day day : set) {
            try {
                Regulator regulator = regulatorRepository.findByScheduleAndDay(schedule, day);
                if (regulator.getDepartureTime() == null) {
                    schedule.getPeriodical().remove(day);
                    regulatorRepository.delete(regulator);
                    scheduleRepository.save(schedule);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        Set<Integer> set1 = regulatorService.getDaysIdList(schedule);
        for (Integer day : days) {
            if (day == 0 || set1.contains(day)) {
                continue;
            }
            Regulator regulator = new Regulator();
            regulator.setSchedule(schedule);
            regulator.setDay(dayRepository.findById(day).get());
            regulatorRepository.save(regulator);
            schedule.getPeriodical().add(dayRepository.findById(day).get());
        }
        scheduleRepository.save(schedule);
        return "redirect:/admin/routes";
    }

    @PostMapping(value = "/routes", params = {"schedule"})
    public String routesDaysManaging(Model model, @RequestParam("schedule") int id) {
        Schedule schedule = scheduleRepository.findById(id).get();
        List<Regulator> regulators = regulatorRepository.findAllBySchedule(schedule);
        model.addAttribute("regs", regulators);
        model.addAttribute("schedule", schedule);
        return "managing";
    }

    @PostMapping(value = "/routes", params = {"regulator", "date_from", "date_to", "dep_time", "arr_time"})
    @Transactional
    public String flightCreator(Model model, @RequestParam("regulator") int id,
                                   @RequestParam("date_from") String date_from,
                                   @RequestParam("date_to") String date_to,
                                   @RequestParam("dep_time") String dep_time,
                                   @RequestParam("arr_time") String arr_time) throws ParseException {

        Regulator regulator = regulatorRepository.findById(id).get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        regulator.setValidFromDate(LocalDate.parse(date_from, formatter));
        regulator.setValidToDate(LocalDate.parse(date_to, formatter));

        regulator.setDepartureTime(new Time(Integer.parseInt(dep_time.substring(0, 2)),
                                            Integer.parseInt(dep_time.substring(3, 5)),
                                            0));
        regulator.setArriveTime(new Time(Integer.parseInt(arr_time.substring(0, 2)),
                                            Integer.parseInt(arr_time.substring(3, 5)),
                                            0));

        regulatorRepository.save(regulator);

        List<LocalDate> dates = dateSelector.getDatesByWeekdayAndBetweenTwoDays(regulator.getDay().getId(), regulator.getValidFromDate(), regulator.getValidToDate());

        for (LocalDate date : dates) {
            Flight flight = new Flight();
            flight.setDepDate(date);
            flight.setArrDate(date);

            flight.setPrice(ThreadLocalRandom.current().nextInt(40, 111));
            flight.setPlane(planeRepository.findById(1).get());
            flight.setDepTime(regulator.getDepartureTime());
            flight.setArrTime(regulator.getArriveTime());
            flight.setRegulator(regulator);
            flightRepository.save(flight);
            Set<Place> places = new HashSet<>();
            for (int i = 1; i <= flight.getPlane().getPlaces(); i++) {
                Place place = new Place(false, i);
                placeRepository.save(place);
                places.add(place);
            }
            flight.setPlaces(places);
            flightRepository.save(flight);
        }
        return "redirect:/admin/routes";
    }

    @GetMapping(value="/flight/{id}")
    public String flightInfo(Model model, @PathVariable int id) {
        SelfFlight selfFlight = selfFlightRepository.getById(id);
        model.addAttribute("self_flight", selfFlight);
        return "admin_flight_view";
    }
}
