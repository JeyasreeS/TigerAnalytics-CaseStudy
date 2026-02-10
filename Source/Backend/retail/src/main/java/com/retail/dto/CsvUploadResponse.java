package com.retail.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CsvUploadResponse {

	private int totalRows;
    private int successCount;
    private int failureCount;
    private List<String> errors;
}
