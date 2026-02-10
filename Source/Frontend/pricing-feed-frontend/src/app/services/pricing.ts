import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  PricingRecord,
  PricingSearchCriteria,
  CsvUploadResponse,
  PageResponse
} from '../models/pricing.model';

@Injectable({ providedIn: 'root' })
export class PricingService {

  private baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  
  uploadCsv(file: File): Observable<CsvUploadResponse> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<CsvUploadResponse>(`${this.baseUrl}/upload`, formData);
  }

  
  search(criteria: PricingSearchCriteria): Observable<PageResponse<PricingRecord>> {
    let params = new HttpParams()
      .set('page', criteria.page.toString())
      .set('size', criteria.size.toString());

    if (criteria.storeId)     params = params.set('storeId', criteria.storeId);
    if (criteria.sku)         params = params.set('sku', criteria.sku);
    if (criteria.productName) params = params.set('productName', criteria.productName);
    if (criteria.startDate)   params = params.set('startDate', criteria.startDate);
    if (criteria.endDate)     params = params.set('endDate', criteria.endDate);
    if (criteria.minPrice != null) params = params.set('minPrice', criteria.minPrice.toString());
    if (criteria.maxPrice != null) params = params.set('maxPrice', criteria.maxPrice.toString());

    return this.http.get<PageResponse<PricingRecord>>(`${this.baseUrl}/search`, { params });
  }

  
  update(id: string, record: Partial<PricingRecord>): Observable<PricingRecord> {
    return this.http.put<PricingRecord>(`${this.baseUrl}/${id}`, record);
  }

  
  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}