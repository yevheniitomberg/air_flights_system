package tomberg.fun.spring.air_flights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tomberg.fun.spring.air_flights.entity.Regulator;
import tomberg.fun.spring.air_flights.entity.Route;
import tomberg.fun.spring.air_flights.entity.Schedule;
import tomberg.fun.spring.air_flights.entity.location.Airport;
import tomberg.fun.spring.air_flights.entity.time.Day;
import tomberg.fun.spring.air_flights.repository.*;
import tomberg.fun.spring.air_flights.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        System.out.println(routes);
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
        if (days.get(0) == 0) {
            schedule.getPeriodical().clear();
            regulatorRepository.deleteAllBySchedule(schedule);
            scheduleRepository.save(schedule);
            return "redirect:/admin/routes";
        }

        schedule.getPeriodical().clear();
        regulatorRepository.deleteAllBySchedule(schedule);

        for (Integer day : days) {
            if (day == 0) {
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

/*  @PostMapping(value = "/routes", params = {"regulator", "date_from", "date_to", "dep_time", "arr_time"})
    public String regulatorParsing(Model model, @RequestParam("regulator") int id,
                                   @RequestParam("date_from") String date_from,
                                   @RequestParam("date_to") String date_to,
                                   @RequestParam("dep_time") String dep_time,
                                   @RequestParam("arr_time") String arr_time) {

        System.out.println(id + date_from + date_to + dep_time + arr_time);
        return "redirect:/"
    }*/
}
