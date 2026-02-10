package com.retail.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.retail.dto.PricingRecordDTO;
import com.retail.entity.PricingRecord;
import com.retail.search.PricingDocument;

@Component
public class PricingMapperUtil {

	public PricingRecordDTO toDto (PricingRecord entity) {
		if(null != entity) {
			PricingRecordDTO dto = new PricingRecordDTO();
			dto.setId(entity.getId());
			dto.setStoreId(entity.getStoreId());
			dto.setSku(entity.getSku());
			dto.setProductName(entity.getProductName());
			dto.setPrice(entity.getPrice());
			dto.setDate(entity.getDate());
			dto.setCreatedAt(entity.getCreatedAt());
			dto.setUpdatedAt(entity.getUpdatedAt());
			return dto;
		}
		else {
			return null;
		}
	}
	
	public List<PricingRecordDTO> toDtoList (List<PricingRecord> entities) {
		
		List<PricingRecordDTO> dtoList = entities.stream().map(this::toDto).toList();
		return dtoList;
	}
	
	public PricingRecord toDto (PricingRecordDTO dto) {
		if(null != dto) {
			PricingRecord entity = new PricingRecord();
			entity.setId(dto.getId());
			entity.setStoreId(dto.getStoreId());
			entity.setSku(dto.getSku());
			entity.setProductName(dto.getProductName());
			entity.setPrice(dto.getPrice());
			entity.setDate(dto.getDate());
			entity.setCreatedAt(dto.getCreatedAt());
			entity.setUpdatedAt(dto.getUpdatedAt());
			return entity;
		}
		else {
			return null;
		}
	}
	
	public void updateEntity(PricingRecord existing, PricingRecordDTO dto) {
        if (dto == null || existing == null) return;

        existing.setStoreId(dto.getStoreId());
        existing.setSku(dto.getSku());
        existing.setProductName(dto.getProductName());
        existing.setPrice(dto.getPrice());
        existing.setDate(dto.getDate());
    }
	
	public PricingDocument toDocument (PricingRecord entity) {
		if(null != entity) {
			PricingDocument document = new PricingDocument();
			document.setId(entity.getId());
			document.setStoreId(entity.getStoreId());
			document.setSku(entity.getSku());
			document.setProductName(entity.getProductName());
			document.setPrice(entity.getPrice());
			document.setDate(entity.getDate());
			return document;
		}
		else {
			return null;
		}
	}
	
	public List<PricingDocument> toDocumentList (List<PricingRecord> entities) {
		
		List<PricingDocument> documentList = entities.stream().map(this::toDocument).toList();
		return documentList;
	}
	
	public PricingRecordDTO toDto (PricingDocument document) {
		if(null != document) {
			PricingRecordDTO dto = new PricingRecordDTO();
			dto.setId(document.getId());
			dto.setStoreId(document.getStoreId());
			dto.setSku(document.getSku());
			dto.setProductName(document.getProductName());
			dto.setPrice(document.getPrice());
			dto.setDate(document.getDate());
			return dto;
		}
		else {
			return null;
		}
	}
	
	public List<PricingRecordDTO> toDtoListFromDocument (List<PricingDocument> documents) {
		
		List<PricingRecordDTO> dtoList = documents.stream().map(this::toDto).toList();
		return dtoList;
	}
	
}
