package homework4.resource;

import homework4.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/login")
    public String userDetails() {
        return "/login";
    }

    @GetMapping("/registration")
    public String userRegistrationPage(){
        return "registration";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @PostMapping("/create")
    public String createNewUser(@RequestParam String username,@RequestParam String password){
        userDetailsService.createUser(username,password);
        return "redirect:/registration";
    }
}
