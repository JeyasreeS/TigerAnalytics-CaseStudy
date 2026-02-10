package com.retail.repository.elasticsearch;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.retail.search.PricingDocument;

@Repository
public interface PricingSearchRepository extends ElasticsearchRepository<PricingDocument, UUID>{

}
