package com.retail.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class PricingSearchCriteria {

	    private String storeId;
	    private String sku;
	    private String productName;    
	    private BigDecimal minPrice;
	    private BigDecimal maxPrice;
	    private LocalDate startDate;
	    private LocalDate endDate;
}
