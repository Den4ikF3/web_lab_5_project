package edu.lab5.controller;

import edu.lab5.model.Repair;
import edu.lab5.repo.CarRepository;
import edu.lab5.repo.RepairRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/repairs")
public class RepairController {

    private static final List<String> STATUSES = List.of("Нове", "В роботі", "Виконано");

    private final RepairRepository repairs;
    private final CarRepository cars;

    public RepairController(RepairRepository repairs, CarRepository cars) {
        this.repairs = repairs;
        this.cars = cars;
    }

    private void fillLists(Model model) {
        model.addAttribute("repairs", repairs.findAll(Sort.by("id")));
        model.addAttribute("cars", cars.findAll(Sort.by("id")));
        model.addAttribute("statuses", STATUSES);
    }

    @GetMapping
    public String list(@RequestParam(required = false) Integer edit, Model model) {
        Repair repair = edit == null ? new Repair() : repairs.findById(edit).orElseGet(Repair::new);
        model.addAttribute("repair", repair);
        fillLists(model);
        return "repairs";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("repair") Repair repair, BindingResult result,
                       Model model, RedirectAttributes redirect) {
        if (!result.hasErrors()) {
            try {
                boolean isNew = repair.getId() == null;
                repairs.saveAndFlush(repair);
                redirect.addFlashAttribute("message", isNew ? "Ремонт додано" : "Зміни збережено");
                return "redirect:/repairs";
            } catch (DataIntegrityViolationException e) {
                result.reject("db", "Дані не відповідають обмеженням бази даних");
            }
        }
        fillLists(model);
        return "repairs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirect) {
        repairs.deleteById(id);
        redirect.addFlashAttribute("message", "Ремонт видалено");
        return "redirect:/repairs";
    }
}
