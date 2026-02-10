package com.retail.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.retail.dto.CsvUploadResponse;
import com.retail.dto.PricingRecordDTO;
import com.retail.dto.PricingSearchCriteria;
import com.retail.entity.PricingRecord;
import com.retail.exception.CsvParseException;
import com.retail.exception.ResourceNotFoundException;
import com.retail.repository.jpa.PricingRecordRepository;
import com.retail.search.PricingDocument;
import com.retail.util.CsvParseResultHelperUtil;
import com.retail.util.PricingMapperUtil;

@Service
@Transactional
public class PricingService {
	
	@Autowired
	public PricingRecordRepository jpaRepository;
	
	@Autowired
    public PricingIndexService indexService;
	
	@Autowired
    public CsvParserService csvParser;
	
	@Autowired
    public ElasticsearchOperations esOperations;
	
	@Autowired
	public PricingMapperUtil mapperUtil;
	
	public CsvUploadResponse uploadCsv(MultipartFile file) {
        CsvParseResultHelperUtil result = null;
		try {
			result = csvParser.parse(file);
		} catch (IOException e) {
			throw new CsvParseException(e.getMessage(), e.getCause());
		}
        List<PricingRecord> toSave = new ArrayList<>();
        
        for (PricingRecord record : result.getValidRecords()) {
            
            Optional<PricingRecord> existing = jpaRepository
                .findByStoreIdAndSkuAndDate(
                    record.getStoreId(), 
                    record.getSku(), 
                    record.getDate());
            
            if (existing.isPresent()) {
                
                PricingRecord entity = existing.get();
                entity.setProductName(record.getProductName());
                entity.setPrice(record.getPrice());
                toSave.add(entity);
            } else {
                
                toSave.add(record);
            }
        }
        
        List<PricingRecord> saved = jpaRepository.saveAll(toSave);
        indexService.bulkIndexRecord(saved);
        
        return new CsvUploadResponse(
            result.getValidRecords().size() + result.getErrors().size(),
            saved.size(),
            result.getErrors().size(),
            result.getErrors()
        );
    }
	
	public Page<PricingRecordDTO> search(PricingSearchCriteria criteria, Pageable pageable) {
		NativeSearchQuery  query = buildSearchQuery(criteria, pageable);

	    SearchHits<PricingDocument> searchHits =
	    		esOperations.search(query, PricingDocument.class);

	    List<PricingRecordDTO> dtos = searchHits.getSearchHits().stream()
	        .map(SearchHit::getContent)
	        .map(mapperUtil::toDto)
	        .toList();

	    long totalHits = searchHits.getTotalHits();

	    return new PageImpl<>(dtos, pageable, totalHits);
    }
	
	private NativeSearchQuery buildSearchQuery(PricingSearchCriteria criteria, Pageable pageable) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

	    if (criteria.getStoreId() != null && !criteria.getStoreId().isEmpty()) {
	        boolQuery.filter(QueryBuilders.termQuery("storeId", criteria.getStoreId()));
	    }

	    if (criteria.getSku() != null && !criteria.getSku().isEmpty()) {
	        boolQuery.filter(QueryBuilders.termQuery("sku", criteria.getSku()));
	    }

	    if (criteria.getProductName() != null && !criteria.getProductName().isEmpty()) {
	        boolQuery.must(QueryBuilders.matchQuery("productName", criteria.getProductName()));
	    }

	    if (criteria.getStartDate() != null || criteria.getEndDate() != null) {
	        var dateRange = QueryBuilders.rangeQuery("date");
	        if (criteria.getStartDate() != null) {
	            dateRange.gte(criteria.getStartDate().toString());
	        }
	        if (criteria.getEndDate() != null) {
	            dateRange.lte(criteria.getEndDate().toString());
	        }
	        boolQuery.filter(dateRange);
	    }

	    if (criteria.getMinPrice() != null || criteria.getMaxPrice() != null) {
	        var priceRange = QueryBuilders.rangeQuery("price");
	        if (criteria.getMinPrice() != null) {
	            priceRange.gte(criteria.getMinPrice());
	        }
	        if (criteria.getMaxPrice() != null) {
	            priceRange.lte(criteria.getMaxPrice());
	        }
	        boolQuery.filter(priceRange);
	    }

	    return new NativeSearchQueryBuilder()
	        .withQuery(boolQuery)
	        .withPageable(pageable)
	        .build();
    }
	
	
    public PricingRecordDTO getById(UUID id) {
        PricingRecord record = jpaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Record not found: " + id));
        return mapperUtil.toDto(record);
    }

    
    public PricingRecordDTO update(UUID id, PricingRecordDTO dto) {
        PricingRecord existing = jpaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Record not found: " + id));
        mapperUtil.updateEntity(existing, dto);
        PricingRecord saved = jpaRepository.save(existing);
        indexService.indexRecord(saved);
        return mapperUtil.toDto(saved);
    }

    
    public void delete(UUID id) {
        if (!jpaRepository.existsById(id))
            throw new ResourceNotFoundException("Record not found: " + id);
        jpaRepository.deleteById(id);
        indexService.removeFromIndex(id);
    }


}
