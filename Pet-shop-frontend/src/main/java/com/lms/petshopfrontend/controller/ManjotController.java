package com.lms.petshopfrontend.controller;

import org.example.frontend.dto.TransactionDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ManjotController {

    @GetMapping("/")
    public String TransactionsList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String searchBy,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9090/api/v1/transaction_history";

        ResponseEntity<List<TransactionDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TransactionDto>>() {}
        );

        List<TransactionDto> data = response.getBody();


        if (status != null && !status.isEmpty()) {
            data = data.stream()
                    .filter(tx -> status.equalsIgnoreCase(tx.getTransactionStatus()))
                    .toList();
        }


        if (searchTerm != null && !searchTerm.isEmpty()) {
            String lowerSearchTerm = searchTerm.toLowerCase();

            if ("petName".equalsIgnoreCase(searchBy)) {
                data = data.stream()
                        .filter(tx -> tx.getPetName() != null &&
                                tx.getPetName().toLowerCase().startsWith(lowerSearchTerm))
                        .toList();
            } else {
                data = data.stream()
                        .filter(tx -> tx.getCustomerName() != null &&
                                tx.getCustomerName().toLowerCase().startsWith(lowerSearchTerm))
                        .toList();
            }
        }

        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            data = data.stream()
                    .filter(tx -> tx.getTransactionDate() != null &&
                            !tx.getTransactionDate().isBefore(start) &&
                            !tx.getTransactionDate().isAfter(end))
                    .toList();
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, data.size());
        List<TransactionDto> paginated = data.subList(start, end);
        int totalPages = (int) Math.ceil((double) data.size() / size);

        model.addAttribute("apiData", paginated);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "TransactionsList";
    }


}
