package com.retail.repository.jpa;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retail.entity.PricingRecord;

@Repository
public interface PricingRecordRepository extends JpaRepository<PricingRecord, UUID> {

	Optional<PricingRecord> findByStoreIdAndSkuAndDate(
	        String storeId, String sku, LocalDate date);
}
