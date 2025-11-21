package com.example.cosmocats.dto.order;

import com.example.cosmocats.dto.validation.orderStatus.ValidOrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class UpdateOrderStatusRequestDto {

    @NotBlank(message = "is required")
    @Size(min = 36, max = 36, message = "must be a valid UUID")
    String orderId;

    @NotBlank(message = "is required")
    @ValidOrderStatus
    String status;
}
