package btl.carrentalwebsite.model;

import jakarta.validation.constraints.*;

public class Staff extends User{

    @NotNull(message = "Lương không được để trống!")
    @Positive(message = "Lương phải là số dương!")
    private Float salary;

    public Staff() {}

//    Getters và Setters
    public Float getSalary() { return salary; }
    public void setSalary(Float salary) { this.salary = salary; }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}
