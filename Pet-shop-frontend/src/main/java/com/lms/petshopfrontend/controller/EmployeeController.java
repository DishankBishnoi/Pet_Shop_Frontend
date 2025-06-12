package org.example.frontend.controller;

import org.example.frontend.dto.EmployeeDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final WebClient webClient = WebClient.create("http://localhost:8080/api/v1");

    // ─── 1. LIST ALL ─────────────────────────────────────────────────────────────
    @GetMapping
    public String listEmployees(Model model) {
        List<EmployeeDto> employees = webClient.get()
                .uri("/employees")
                .retrieve()
                .bodyToFlux(EmployeeDto.class)
                .collectList()
                .block();

        model.addAttribute("employees", employees);
        return "employees";  // renders employees.html
    }

    // ─── 2. SHOW ADD FORM ───────────────────────────────────────────────────────
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new EmployeeDto());
        return "add-employee"; // renders add-employee.html
    }

    // ─── 3. HANDLE ADD ─────────────────────────────────────────────────────────
    @PostMapping("/add")
    public String doAdd(@ModelAttribute EmployeeDto employee) {
        webClient.post()
                .uri("/employees/add")  // must match backend @PostMapping("/add")
                .body(Mono.just(employee), EmployeeDto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        return "redirect:/employees";
    }

    // ─── 4. SHOW EDIT FORM ──────────────────────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        EmployeeDto emp = webClient.get()
                .uri("/employees/{id}", id)
                .retrieve()
                .bodyToMono(EmployeeDto.class)
                .block();
        model.addAttribute("employee", emp);
        return "edit-employee"; // renders edit-employee.html
    }

    // ─── 5. HANDLE EDIT ─────────────────────────────────────────────────────────
    @PostMapping("/edit")
    public String doEdit(@ModelAttribute EmployeeDto employee) {
        webClient.put()
                .uri("/employees/update/{id}", employee.getId())  // must match backend @PutMapping("/update/{id}")
                .body(Mono.just(employee), EmployeeDto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        return "redirect:/employees";
    }
}
