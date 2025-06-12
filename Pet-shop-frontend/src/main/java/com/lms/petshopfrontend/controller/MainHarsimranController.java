package com.lms.petshopfrontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainHarsimranController{

    @GetMapping("/vaccination")
    public String index() {
        return "harsimran/index";
    }
    @GetMapping("/index")
    public String employeePortalPage() {
        return "harsimran/index"; // Make sure employeePortal.html is also in /templates
    }
}
