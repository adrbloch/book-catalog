package io.github.adrbloch.bookcatalog.controller;

import io.github.adrbloch.bookcatalog.model.User;
import io.github.adrbloch.bookcatalog.exception.FieldsNotMatchException;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
class UserController {

    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    String homePage() {
        return "home";
    }

    @GetMapping("/login")
    String loginPage() {
        return "login";
    }


    @GetMapping("/registration")
    String addUserForm(Model model) {

        var newUser = new User();
        model.addAttribute("user", newUser);

        return "registration";
    }

    @PostMapping("/save")
    String saveUser(@ModelAttribute("user") @Valid User userToSave,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors())
            return "registration";

        try {
            userService.createUser(userToSave);

        } catch (ResourceAlreadyExistsException e) {
            model.addAttribute("occurredUserException", true);
            model.addAttribute("exceptionUserMessage", e.getMessage());
            return "registration";

        } catch (FieldsNotMatchException e){
            model.addAttribute("occurredPasswordException", true);
            model.addAttribute("exceptionPasswordMessage", e.getMessage());
            return "registration";
        }

        return "redirect:/login";
    }


}
