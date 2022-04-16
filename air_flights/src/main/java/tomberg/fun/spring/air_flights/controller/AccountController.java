package tomberg.fun.spring.air_flights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tomberg.fun.spring.air_flights.entity.User;
import tomberg.fun.spring.air_flights.repository.FlightsRepository;
import tomberg.fun.spring.air_flights.repository.UserRepository;
import tomberg.fun.spring.air_flights.service.UserService;

@Controller
public class AccountController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    FlightsRepository flightsRepository;

    @GetMapping("/account")
    public String accountView(Model model) {
        User user = userRepository.findByEmail(userService.getCurrentEmail());
        model.addAttribute("user", user);
        model.addAttribute("info", user.getUserInfo());
        model.addAttribute("flights", flightsRepository.findAllByUser(user));
        return "account";
    }
}
