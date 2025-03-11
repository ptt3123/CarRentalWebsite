package btl.carrentalwebsite.model;

import jakarta.validation.constraints.*;
import java.io.Serializable;

class BrokenReport implements Serializable {
    
    private static final long serialVersionUID = 1L;
   
    private int id;
    
    @Size(max = 255, message = "Detail không được quá 255 ký tự!")
    private String detail;
    
    @NotNull(message = "Giá tiền không được Null!")
    @Positive(message = "Giá phải là số dương!")
    private double price;
    
    @NotNull(message = "carId không được Null!")
    @Min(value = 1, message = "carId không hợp lệ")
    private int carId;
    
    public BrokenReport() {}
    
    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getDetail() {
        return detail;
    }

    public double getPrice() {
        return price;
    }

    public int getCarId() {
        return carId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    @Override
    public String toString() {
        return "BrokenReport{" + "id=" + id + ", detail=" + detail + ", price=" + price + ", carId=" + carId + '}';
    }
    
    
}