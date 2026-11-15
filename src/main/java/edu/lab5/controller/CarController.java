package edu.lab5.controller;

import edu.lab5.model.Car;
import edu.lab5.repo.CarRepository;
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

@Controller
@RequestMapping("/cars")
public class CarController {

    private final CarRepository cars;

    public CarController(CarRepository cars) {
        this.cars = cars;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Integer edit, Model model) {
        Car car = edit == null ? new Car() : cars.findById(edit).orElseGet(Car::new);
        model.addAttribute("car", car);
        model.addAttribute("cars", cars.findAll(Sort.by("id")));
        return "cars";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("car") Car car, BindingResult result,
                       Model model, RedirectAttributes redirect) {
        if (!result.hasErrors()) {
            try {
                boolean isNew = car.getId() == null;
                cars.saveAndFlush(car);
                redirect.addFlashAttribute("message", isNew ? "Автомобіль додано" : "Зміни збережено");
                return "redirect:/cars";
            } catch (DataIntegrityViolationException e) {
                result.rejectValue("plate", "duplicate", "Автомобіль з таким держномером вже існує");
            }
        }
        model.addAttribute("cars", cars.findAll(Sort.by("id")));
        return "cars";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirect) {
        cars.deleteById(id);
        redirect.addFlashAttribute("message", "Автомобіль видалено");
        return "redirect:/cars";
    }
}
