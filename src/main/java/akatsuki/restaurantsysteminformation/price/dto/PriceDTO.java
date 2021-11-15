package akatsuki.restaurantsysteminformation.price.dto;

import akatsuki.restaurantsysteminformation.price.Price;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PriceDTO {

    private LocalDateTime createdAt;
    private double value;

    public PriceDTO(Price price) {
        this.createdAt = price.getCreatedAt();
        this.value = price.getValue();
    }
}
