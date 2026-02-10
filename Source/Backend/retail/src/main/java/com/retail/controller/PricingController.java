package com.retail.controller;

import java.io.IOException;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.retail.dto.CsvUploadResponse;
import com.retail.dto.PricingRecordDTO;
import com.retail.dto.PricingSearchCriteria;
import com.retail.service.PricingService;

@RestController
@RequestMapping("/pricing")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class PricingController {

	@Autowired
	public PricingService pricingService;

	@PostMapping(value = "/upload", produces = "application/json")
	public ResponseEntity<CsvUploadResponse> uploadCsv(@RequestParam("file") MultipartFile file) {
		CsvUploadResponse response = pricingService.uploadCsv(file);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/search")
	public ResponseEntity<Page<PricingRecordDTO>> search(PricingSearchCriteria criteria,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
			@RequestParam(defaultValue = "date") String sortBy, @RequestParam(defaultValue = "desc") String sortDir) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
		return ResponseEntity.ok(pricingService.search(criteria, pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PricingRecordDTO> getById(@PathVariable UUID id) {
		return ResponseEntity.ok(pricingService.getById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<PricingRecordDTO> update(@PathVariable UUID id, @Valid @RequestBody PricingRecordDTO dto) {
		return ResponseEntity.ok(pricingService.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		pricingService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
