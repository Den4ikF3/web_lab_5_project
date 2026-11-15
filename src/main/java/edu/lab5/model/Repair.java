package edu.lab5.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "repairs")
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Оберіть автомобіль")
    @Column(name = "car_id")
    private Integer carId;

    /** Автомобіль (лише для читання, зв'язок задається полем carId). */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", insertable = false, updatable = false)
    private Car car;

    @NotBlank(message = "Вкажіть опис робіт")
    @Size(max = 200, message = "Не більше 200 символів")
    private String description;

    @NotNull(message = "Вкажіть вартість")
    @DecimalMin(value = "0.00", message = "Вартість не може бути від’ємною")
    private BigDecimal price;

    @NotNull(message = "Вкажіть дату")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "repair_date")
    private LocalDate repairDate = LocalDate.now();

    @NotBlank(message = "Вкажіть статус")
    private String status = "Нове";

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getCarId() { return carId; }
    public void setCarId(Integer carId) { this.carId = carId; }
    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public LocalDate getRepairDate() { return repairDate; }
    public void setRepairDate(LocalDate repairDate) { this.repairDate = repairDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
