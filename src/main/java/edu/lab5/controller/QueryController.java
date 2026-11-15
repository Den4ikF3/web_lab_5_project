package edu.lab5.controller;

import edu.lab5.service.QueryService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/query")
public class QueryController {

    private final QueryService service;

    public QueryController(QueryService service) {
        this.service = service;
    }

    @GetMapping
    public String form(@RequestParam(defaultValue = "0") int example, Model model) {
        int index = Math.max(0, Math.min(example, QueryService.EXAMPLES.size() - 1));
        model.addAttribute("sql", QueryService.EXAMPLES.get(index));
        return "query";
    }

    @PostMapping
    public String run(@RequestParam String sql, Model model) {
        model.addAttribute("sql", sql);
        try {
            model.addAttribute("result", service.run(sql));
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        } catch (DataAccessException e) {
            model.addAttribute("error", "Помилка виконання запиту: " + e.getMostSpecificCause().getMessage());
        }
        return "query";
    }
}
