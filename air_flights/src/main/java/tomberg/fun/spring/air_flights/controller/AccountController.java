package tomberg.fun.spring.air_flights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tomberg.fun.spring.air_flights.entity.Gender;
import tomberg.fun.spring.air_flights.entity.User;
import tomberg.fun.spring.air_flights.entity.UserInfo;
import tomberg.fun.spring.air_flights.repository.FlightsRepository;
import tomberg.fun.spring.air_flights.repository.GenderRepository;
import tomberg.fun.spring.air_flights.repository.UserInfoRepository;
import tomberg.fun.spring.air_flights.repository.UserRepository;
import tomberg.fun.spring.air_flights.service.UserInfoService;
import tomberg.fun.spring.air_flights.service.UserService;

@Controller
public class AccountController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    FlightsRepository flightsRepository;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/account")
    public String accountView(Model model) {
        User user = userRepository.findByEmail(userService.getCurrentEmail());
        model.addAttribute("user", user);
        model.addAttribute("info", user.getUserInfo());
        model.addAttribute("flights", flightsRepository.findAllByUser(user));
        return "account";
    }

    @PostMapping("/account")
    public String accountOperations(@RequestParam("action") String action, Model model) {
        User user = userRepository.findByEmail(userService.getCurrentEmail());
        if (action.equals("update")) {
            model.addAttribute("user", user);
            return "redirect:/account/update_info";
        }
        if (action.equals("book")) {
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
}
