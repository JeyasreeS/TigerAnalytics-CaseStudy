package com.retail.search;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import org.springframework.data.annotation.Id;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.MultiField;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.InnerField;

@Data
@Document(indexName = "pricing_records")
public class PricingDocument {
	
	@Id
    private UUID id;

    @Field(type = FieldType.Keyword)
    private String storeId;

    @Field(type = FieldType.Keyword)
    private String sku;

    @MultiField(
    	    mainField = @Field(type = FieldType.Text, analyzer = "product_name_analyzer"),
    	    otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String productName;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Field(type = FieldType.Date, format = DateFormat.year_month_day)
    private LocalDate date;
}
