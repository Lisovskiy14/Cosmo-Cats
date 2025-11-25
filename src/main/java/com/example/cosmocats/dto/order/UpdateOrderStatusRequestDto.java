package com.example.cosmocats.dto.order;

import com.example.cosmocats.dto.validation.orderStatus.ValidOrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class UpdateOrderStatusRequestDto {

    @NotBlank(message = "is required")
    @ValidOrderStatus
    String status;
}
