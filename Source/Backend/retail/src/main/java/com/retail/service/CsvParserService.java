package com.retail.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.retail.entity.PricingRecord;
import com.retail.exception.CsvParseException;
import com.retail.util.CsvParseResultHelperUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CsvParserService {

	private static final String[] EXPECTED_HEADERS = {
	        "Store ID", "SKU", "Product Name", "Price", "Date"
	    };

	    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    
	    public CsvParseResultHelperUtil parse(MultipartFile file) throws IOException {

	        
	        if (file.isEmpty()) {
	            throw new CsvParseException("Uploaded file is empty");
	        }

	        
	        String filename = file.getOriginalFilename();
	        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
	            throw new CsvParseException("File must have .csv extension");
	        }

	        CsvParseResultHelperUtil result = new CsvParseResultHelperUtil();

	        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
	        if (content.startsWith("\uFEFF")) {
	            content = content.substring(1);
	        }
	        try (BufferedReader bufferedReader = new BufferedReader(
	                 new InputStreamReader(file.getInputStream()));
	        		CSVReader csvReader = new CSVReader(new StringReader(content))) {

	            
	            String[] headers = csvReader.readNext();
	            if (headers == null) {
	                throw new CsvParseException("CSV file has no content");
	            }

	            
	            String[] trimmedHeaders = Arrays.stream(headers)
	                .map(String::trim)
	                .toArray(String[]::new);

	            if (!Arrays.equals(trimmedHeaders, EXPECTED_HEADERS)) {
	                throw new CsvParseException(
	                    "Invalid CSV headers. Expected: " + Arrays.toString(EXPECTED_HEADERS)
	                    + " but got: " + Arrays.toString(trimmedHeaders));
	            }

	            
	            String[] row;
	            int rowNumber = 1; 

	            while ((row = csvReader.readNext()) != null) {
	                rowNumber++;
	                parseRow(row, rowNumber, result);
	            }

	            
	            if (result.getTotalRows() == 0) {
	                throw new CsvParseException("CSV file has headers but no data rows");
	            }

	        } catch (CsvParseException e) {
	            throw e; 
	        } catch (CsvValidationException e) {
	            throw new CsvParseException("CSV format error: " + e.getMessage());
	        } catch (Exception e) {
	            throw new CsvParseException("Failed to read CSV file: " + e.getMessage());
	        }

	        log.info("CSV parsing complete. Valid: {}, Errors: {}",
	            result.getValidRecords().size(), result.getErrors().size());

	        return result;
	    }

	    /**
	     * 
	     */
	    private void parseRow(String[] row, int rowNumber, CsvParseResultHelperUtil result) {

	        
	        if (row.length < 5) {
	            result.addError("Row " + rowNumber + ": Expected 5 columns but found " + row.length);
	            return;
	        }

	        
	        String storeId    = row[0].trim();
	        String sku        = row[1].trim();
	        String productName = row[2].trim();
	        String priceStr   = row[3].trim();
	        String dateStr    = row[4].trim();

	        
	        StringBuilder rowErrors = new StringBuilder();

	        
	        if (storeId.isEmpty()) {
	            rowErrors.append("Store ID is blank; ");
	        }

	        
	        if (sku.isEmpty()) {
	            rowErrors.append("SKU is blank; ");
	        }

	        
	        if (productName.isEmpty()) {
	            rowErrors.append("Product Name is blank; ");
	        }

	        
	        BigDecimal price = null;
	        if (priceStr.isEmpty()) {
	            rowErrors.append("Price is blank; ");
	        } else {
	            try {
	                price = new BigDecimal(priceStr);
	                if (price.compareTo(BigDecimal.ZERO) <= 0) {
	                    rowErrors.append("Price must be greater than 0, got: " + priceStr + "; ");
	                    price = null;
	                }
	            } catch (NumberFormatException e) {
	                rowErrors.append("Invalid price format: '" + priceStr + "'; ");
	            }
	        }

	        
	        LocalDate date = null;
	        if (dateStr.isEmpty()) {
	            rowErrors.append("Date is blank; ");
	        } else {
	            try {
	                date = LocalDate.parse(dateStr, DATE_FORMAT);
	            } catch (DateTimeParseException e) {
	                rowErrors.append("Invalid date format: '" + dateStr + "', expected yyyy-MM-dd; ");
	            }
	        }

	        
	        if (rowErrors.length() > 0) {
	            result.addError("Row " + rowNumber + ": " + rowErrors.toString().trim());
	        } else {
	            PricingRecord record = new PricingRecord();
	            record.setStoreId(storeId);
	            record.setSku(sku);
	            record.setProductName(productName);
	            record.setPrice(price);
	            record.setDate(date);
	            result.addValidRecord(record);
	        }
	    }

}
