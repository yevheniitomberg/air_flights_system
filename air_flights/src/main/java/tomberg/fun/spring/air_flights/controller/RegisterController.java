package tomberg.fun.spring.air_flights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import tomberg.fun.spring.air_flights.entity.User;
import tomberg.fun.spring.air_flights.repository.UserRepository;
import tomberg.fun.spring.air_flights.service.EmailService;
import tomberg.fun.spring.air_flights.service.UserService;

import javax.validation.Valid;

@Controller
public class RegisterController {
    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/register")
    public  String registerView(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        System.out.println(user);
        if (!user.getPassword().equals(user.getCommitPassword())) {
            ObjectError error = new ObjectError("pass_not_eq", "Passwords do not match");
            bindingResult.addError(error);
        }
        if (userService.emailAlreadyExists(user)) {
            ObjectError error = new ObjectError("email_already_exist", "This email is already registered!");
            bindingResult.addError(error);
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (emailService.registrationConfirmEmail(user)) {
            ObjectError error = new ObjectError("email_incorrect", "This email is incorrect!");
            bindingResult.addError(error);
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        model.addAttribute("email_was_sent", true);
        userService.saveUser(user);
        return "register";
    }

    @GetMapping("/register/confirm/{confirmLink}")
    public String confirmingRegistrationView(@PathVariable(value = "confirmLink") String confirmLink, Model model) {
        User user = userRepository.findByConfirmLink(confirmLink);
        model.addAttribute("user", user);
        return "confirm";
    }

    @PostMapping("/register/confirm/{confirmLink}")
    public String confirmingRegistration(@PathVariable(value = "confirmLink") String confirmLink) {
        User user = userRepository.findByConfirmLink(confirmLink);
        user.setFullyRegistered(true);
        userRepository.saveAndFlush(user);
        return "redirect:/login";
    }
}
