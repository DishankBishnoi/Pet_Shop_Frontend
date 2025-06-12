package org.example.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/index")
    public String employeePortalPage() {
        return "index"; // Make sure employeePortal.html is also in /templates
    }
}
