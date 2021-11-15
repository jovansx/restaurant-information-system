package akatsuki.restaurantsysteminformation.price.dto;

import akatsuki.restaurantsysteminformation.price.Price;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PriceDTO {

    private LocalDateTime createdAt;
    private double value;

    public PriceDTO(Price price) {
        this.createdAt = price.getCreatedAt();
        this.value = price.getValue();
    }
}
