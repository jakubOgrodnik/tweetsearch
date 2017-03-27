package pl.kuba.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginCotroller {
    @RequestMapping("/login")
    public String authenticate() {
        return "login";
    }
}
