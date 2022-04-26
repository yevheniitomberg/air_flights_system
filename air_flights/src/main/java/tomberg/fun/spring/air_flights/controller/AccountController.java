package tomberg.fun.spring.air_flights.controller;

import org.hibernate.mapping.Bag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tomberg.fun.spring.air_flights.entity.*;
import tomberg.fun.spring.air_flights.entity.location.Airport;
import tomberg.fun.spring.air_flights.repository.*;
import tomberg.fun.spring.air_flights.service.UserInfoService;
import tomberg.fun.spring.air_flights.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@CrossOrigin(allowedHeaders = "*", origins = "http://localhost:8080/")
public class AccountController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    BaggageRepository baggageRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    AirportRepository airportRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    SelfFlightRepository selfFlightRepository;

    @Autowired
    DayRepository dayRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    RegulatorRepository regulatorRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PlaceRepository placeRepository;

    @GetMapping("/account")
    public String accountView(Model model) {
        try {
            List<SelfFlight> selfFlights = selfFlightRepository.findAllByUserAndPaidFalse(userRepository.findByEmail(userService.getCurrentEmail()));
            for (SelfFlight selfFlight : selfFlights) {
                if (!selfFlight.isPaid()) {
                    try {
                        selfFlight.getPlace().setBooked(false);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
            selfFlightRepository.deleteAll(selfFlights);
        } catch (Exception e) {
            System.out.println(e);
        }
        User user = userRepository.findByEmail(userService.getCurrentEmail());
        model.addAttribute("user", user);
        model.addAttribute("info", user.getUserInfo());
        model.addAttribute("flights", selfFlightRepository.findAllByUserAndPaidTrueOrderByDepDateDesc(user));
        return "account";
    }

    @PostMapping("/account")
    public String accountOperations(@RequestParam("action") String action, Model model) {
        User user = userRepository.findByEmail(userService.getCurrentEmail());
        if (action.equals("update")) {
            model.addAttribute("user", user);
            return "redirect:/account/update_info";
        }
        if (action.equals("booking")) {
            model.addAttribute("user", user);
            return "redirect:/account/book_flight";
        }
        return "account";
    }

    @GetMapping("/account/update_info")
    public String accountUpdateUserInfoView(Model model) {
        User user = userRepository.findByEmail(userService.getCurrentEmail());
        model.addAttribute("user_info", user.getUserInfo());
        model.addAttribute("genders", genderRepository.findAll());
        return "update_info";
    }

    @PostMapping("/account/update_info")
    public String accountUpdateUserInfo(@ModelAttribute("user_info")UserInfo userInfo, @RequestParam("gen") int gen_id) {
        userInfo.setGender(genderRepository.findById(gen_id).get());
        User user = userRepository.findByEmail(userService.getCurrentEmail());
        UserInfo info = user.getUserInfo();
        userInfoService.updateUserInfo(info, userInfo);
        userInfoRepository.saveAndFlush(info);
        return "redirect:/account";
    }

    @GetMapping("/account/book_flight")
    public String bookingFlightView(Model model) {
        List<Airport> list = airportRepository.findAll();
        list.remove(airportRepository.findByAirportCode("000"));
        model.addAttribute("airports", list);
        model.addAttribute("flight", new Regulator());
        return "book_flight";
    }

    @PostMapping(value = "/account/book_flight", params = {"airport_from", "airport_to"})
    public String bookingFlightSelectDateView(Model model,
                                              @RequestParam("airport_from") String airport_from,
                                              @RequestParam("airport_to") String airport_to) {


        SelfFlight selfFlight = new SelfFlight();
        Route route = routeRepository.findByAirportFromIdAndAirportToId(airportRepository.findByAirportCode(airport_from).getId(), airportRepository.findByAirportCode(airport_to).getId());
        User user = userRepository.findByEmail(userService.getCurrentEmail());
        selfFlight.setRoute(route);
        selfFlight.setUser(user);
        selfFlight.setPaid(false);
        selfFlight.setUserInfo(user.getUserInfo());

        selfFlightRepository.save(selfFlight);

        model.addAttribute("from", airport_from);
        model.addAttribute("to", airport_to);
        return "select_date";
    }

    @PostMapping(value = "/account/book_flight", params = {"selected_date"})
    public String bookingFlightSelectedDate(Model model,
                                              @RequestParam("selected_date") String sel_date) {


        SelfFlight selfFlight = selfFlightRepository.findByUserAndPaidFalse(userRepository.findByEmail(userService.getCurrentEmail()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(sel_date, formatter);
        selfFlight.setDepDate(localDate);
        selfFlight.setArrDate(localDate);

        selfFlightRepository.save(selfFlight);

        Schedule schedule = scheduleRepository.findByRoute(selfFlight.getRoute());
        Regulator regulator = regulatorRepository.findByScheduleAndDay(schedule, dayRepository.findById(localDate.getDayOfWeek().getValue()).get());
        Flight flight = flightRepository.findByDepDateAndRegulator(localDate, regulator);

        model.addAttribute("flight", flight);

        return "select_flight";
    }

    @PostMapping(value = "/account/book_flight", params = {"selected_flight"})
    public String bookingFlightSelectedFlight(Model model,
                                            @RequestParam("selected_flight") int sel_flight) {


        SelfFlight selfFlight = selfFlightRepository.findByUserAndPaidFalse(userRepository.findByEmail(userService.getCurrentEmail()));
        Flight flight = flightRepository.findById(sel_flight).get();

        selfFlight.setDepTime(flight.getDepTime());
        selfFlight.setArrTime(flight.getArrTime());
        selfFlight.setFinalCost(flight.getPrice());

        selfFlightRepository.save(selfFlight);

        List<Baggage> baggages = baggageRepository.findAll();
        baggages.remove(baggageRepository.getById(4));

        model.addAttribute("baggs", baggages);
        return "select_baggage";
    }

    @PostMapping(value = "/account/book_flight", params = {"selected_bag"})
    public String bookingFlightSelectedBag(Model model,
                                              @RequestParam("selected_bag") int sel_bag) {

        SelfFlight selfFlight = selfFlightRepository.findByUserAndPaidFalse(userRepository.findByEmail(userService.getCurrentEmail()));

        selfFlight.setBaggage(baggageRepository.getById(sel_bag));
        int cost = selfFlight.getFinalCost();
        selfFlight.setFinalCost(cost + selfFlight.getBaggage().getAdditionalFee());

        selfFlightRepository.save(selfFlight);

        Schedule schedule = scheduleRepository.findByRoute(selfFlight.getRoute());
        Regulator regulator = regulatorRepository.findByScheduleAndDay(schedule, dayRepository.findById(selfFlight.getDepDate().getDayOfWeek().getValue()).get());
        Flight flight = flightRepository.findByDepDateAndRegulator(selfFlight.getDepDate(), regulator);

        Set<Place> set = flight.getPlaces();

        Object[] array = set.toArray();
        Arrays.sort(array);

        List<Place> places = new ArrayList<>();

        for (Object obj: array) {
            places.add((Place) obj);
        }

        model.addAttribute("places", places);
        model.addAttribute("flight", flight);
        return "select_place";
    }

    @PostMapping(value = "/account/book_flight", params = {"flight_id", "place_id"})
    public String bookingFlightSelectedPlace(Model model,
                                             @RequestParam("flight_id") int flight_id,
                                             @RequestParam("place_id") int place_id) {

        SelfFlight selfFlight = selfFlightRepository.findByUserAndPaidFalse(userRepository.findByEmail(userService.getCurrentEmail()));
        Place place = placeRepository.getById(place_id);
        place.setBooked(true);
        placeRepository.save(place);
        selfFlight.setPlace(place);
        selfFlightRepository.save(selfFlight);

        model.addAttribute("user_info", selfFlight.getUserInfo());
        model.addAttribute("genders", genderRepository.findAll());

        return "data_about_passenger";
    }

    @PostMapping(value = "/account/book_flight", params = {"gen"})
    public String bookingFillUserInfo(@ModelAttribute("user_info")UserInfo userInfo,
                                      @RequestParam("gen") int gen_id,
                                      Model model) {

        SelfFlight selfFlight = selfFlightRepository.findByUserAndPaidFalse(userRepository.findByEmail(userService.getCurrentEmail()));
        userInfo.setGender(genderRepository.findById(gen_id).get());

        if (selfFlight.getUserInfo().getPass_num().equals(userInfo.getPass_num())) {
            model.addAttribute("self_flight", selfFlight);
            return "payment";
        } else {
            userInfoRepository.save(userInfo);
            selfFlight.setUserInfo(userInfo);
            selfFlightRepository.save(selfFlight);
            model.addAttribute("self_flight", selfFlight);
            return "payment";
        }
    }

    @PostMapping(value = "/account/book_flight", params = {"payment"})
    public String bookingPayment(@RequestParam("payment") boolean payment, Model model) {

        SelfFlight selfFlight = selfFlightRepository.findByUserAndPaidFalse(userRepository.findByEmail(userService.getCurrentEmail()));
        if (payment) {
            selfFlight.setPaid(true);
            selfFlightRepository.save(selfFlight);
        }
        return  "redirect:/account";
    }
}
