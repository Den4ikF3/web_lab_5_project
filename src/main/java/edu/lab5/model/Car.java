package edu.lab5.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Вкажіть держномер")
    @Size(max = 15, message = "Не більше 15 символів")
    private String plate;

    @NotBlank(message = "Вкажіть марку")
    @Size(max = 50, message = "Не більше 50 символів")
    private String brand;

    @NotBlank(message = "Вкажіть модель")
    @Size(max = 50, message = "Не більше 50 символів")
    private String model;

    @NotNull(message = "Вкажіть рік випуску")
    @Min(value = 1950, message = "Рік має бути не менше 1950")
    @Max(value = 2100, message = "Рік має бути не більше 2100")
    private Integer year;

    @NotBlank(message = "Вкажіть власника")
    @Size(max = 100, message = "Не більше 100 символів")
    @Column(name = "owner_name")
    private String ownerName;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
}
