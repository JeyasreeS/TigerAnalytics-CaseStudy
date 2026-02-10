package com.retail.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PricingRecordDTO {

	private UUID id;

    @NotBlank(message = "Store ID is required")
    private String storeId;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Product Name is required")
    private String productName;

    @NotNull @DecimalMin(value = "0.01", message = "Price must be > 0")
    private BigDecimal price;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
