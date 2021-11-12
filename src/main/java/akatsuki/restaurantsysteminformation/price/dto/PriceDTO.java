package akatsuki.restaurantsysteminformation.price.dto;

import akatsuki.restaurantsysteminformation.price.Price;

import java.time.LocalDateTime;

public class PriceDTO {

    private LocalDateTime createdAt;
    private double value;

    public PriceDTO(Price price) {
        this.createdAt = price.getCreatedAt();
        this.value = price.getValue();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
