package com.retail.util;

import java.util.ArrayList;
import java.util.List;

import com.retail.entity.PricingRecord;

import lombok.Getter;

@Getter
public class CsvParseResultHelperUtil {

	 private final List<PricingRecord> validRecords = new ArrayList<>();
	    private final List<String> errors = new ArrayList<>();

	    public void addValidRecord(PricingRecord record) {
	        validRecords.add(record);
	    }

	    public void addError(String error) {
	        errors.add(error);
	    }

	    public boolean hasErrors() {
	        return !errors.isEmpty();
	    }

	    public int getTotalRows() {
	        return validRecords.size() + errors.size();
	    }
	    
	    public int getTotalValidRows() {
	        return validRecords.size();
	    }
	    
	    public int getTotalErrorRows() {
	        return validRecords.size();
	    }
}
