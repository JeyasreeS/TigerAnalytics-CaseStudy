package com.retail.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.entity.PricingRecord;
import com.retail.repository.elasticsearch.PricingSearchRepository;
import com.retail.search.PricingDocument;
import com.retail.util.PricingMapperUtil;

@Service
public class PricingIndexService {
	
	@Autowired
	public PricingSearchRepository searchRepo;
	
	@Autowired
	public PricingMapperUtil mapperUtil;
	
	public void indexRecord (PricingRecord entity) {
		
		PricingDocument doc = mapperUtil.toDocument(entity);
		
		searchRepo.save(doc);
	}
	
	public void bulkIndexRecord (List<PricingRecord> entities) {
		
		List<PricingDocument> docList = mapperUtil.toDocumentList(entities);
		
		searchRepo.saveAll(docList);
	}
	
	public void removeFromIndex(UUID id) {
		searchRepo.deleteById(id);
    }

}
