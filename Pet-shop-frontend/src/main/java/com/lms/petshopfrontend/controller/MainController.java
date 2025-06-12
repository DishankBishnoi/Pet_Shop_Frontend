package com.lms.petshopfrontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // ✅ ADD THIS
public class MainController {

    @GetMapping("/home")
    public String showCustomerPage() {
        return "index"; // return the name of your HTML file without `.html`
    }

    @GetMapping("/getdata")
    public String showMainHtmlPage() {
        return "htmlMainPage"; // assuming htmlMainPage.html exists in templates
    }
}
