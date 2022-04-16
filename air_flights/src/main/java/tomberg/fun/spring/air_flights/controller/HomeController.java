package tomberg.fun.spring.air_flights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tomberg.fun.spring.air_flights.repository.UserRepository;
import tomberg.fun.spring.air_flights.service.UserService;

@Controller
public class HomeController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String homepageRedirect() {
        return "redirect:/homepage";
    }

    @GetMapping("/homepage")
    public String homepageView(Model model) {
        model.addAttribute("user", userRepository.findByEmail(userService.getCurrentEmail()));
        return "homepage";
    }
}
