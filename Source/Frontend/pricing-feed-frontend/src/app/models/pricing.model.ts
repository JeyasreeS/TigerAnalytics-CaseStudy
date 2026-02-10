export interface PricingRecord {
  id: string;
  storeId: string;
  sku: string;
  productName: string;
  price: number;
  date: string;           
  createdAt?: string;
  updatedAt?: string;
}

export interface PricingSearchCriteria {
  storeId?: string;
  sku?: string;
  productName?: string;
  startDate?: string;
  endDate?: string;
  minPrice?: number;
  maxPrice?: number;
  page: number;
  size: number;
}

export interface CsvUploadResponse {
  totalRows: number;
  successCount: number;
  failureCount: number;
  errors: string[];
}

export interface RowError {
  row: number;
  message: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}