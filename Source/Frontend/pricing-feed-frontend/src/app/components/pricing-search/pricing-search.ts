import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PricingService } from '../../services/pricing';
import {
  PricingRecord,
  PricingSearchCriteria,
  PageResponse
} from '../../models/pricing.model';
import { ConfirmDialog } from '../confirm-dialog/confirm-dialog';
import { PricingEditDialog } from '../pricing-edit-dialog/pricing-edit-dialog';

@Component({
  selector: 'app-pricing-search',
  standalone: true,
  imports: [CommonModule, FormsModule, PricingEditDialog, ConfirmDialog],
  templateUrl: './pricing-search.html',
  styleUrl: './pricing-search.css'
})
export class PricingSearch {
  storeId = '';
  sku = '';
  productName = '';
  startDate = '';
  endDate = '';
  minPrice: number | null = null;
  maxPrice: number | null = null;

  
  records: PricingRecord[] = [];
  totalElements = 0;
  totalPages = 0;
  currentPage = 0;
  pageSize = 20;
  loading = false;
  searched = false;

  
  showEditDialog = false;
  editRecord: PricingRecord | null = null;

  
  showDeleteDialog = false;
  deleteRecord: PricingRecord | null = null;

  constructor(private pricingService: PricingService,
    private cdr: ChangeDetectorRef
  ) {}

  search(): void {
    this.currentPage = 0;
    this.executeSearch();
  }

  executeSearch(): void {
    this.loading = true;

  const criteria: PricingSearchCriteria = {
    page: this.currentPage,
    size: this.pageSize
  };

  if (this.storeId && this.storeId.trim())       criteria.storeId = this.storeId.trim();
  if (this.sku && this.sku.trim())               criteria.sku = this.sku.trim();
  if (this.productName && this.productName.trim()) criteria.productName = this.productName.trim();
  if (this.startDate)   criteria.startDate = this.startDate;
  if (this.endDate)     criteria.endDate = this.endDate;
  if (this.minPrice !== null && this.minPrice !== undefined && this.minPrice > 0)
    criteria.minPrice = this.minPrice;
  if (this.maxPrice !== null && this.maxPrice !== undefined && this.maxPrice > 0)
    criteria.maxPrice = this.maxPrice;

  console.log('Search criteria:', JSON.stringify(criteria)); 

  this.pricingService.search(criteria).subscribe({
    next: (response: any) => {
      console.log('Response:', response);
      this.records = response.content;
      this.totalElements = response.totalElements;
      this.totalPages = response.totalPages;
      this.searched = true;
      this.loading = false;
      this.cdr.detectChanges();
    },
    error: (err) => {
      console.error('Search error:', err);
      this.searched = true;
      this.loading = false;
      this.cdr.detectChanges();  
    }
  });
  }

  clearFilters(): void {
    this.storeId = '';
    this.sku = '';
    this.productName = '';
    this.startDate = '';
    this.endDate = '';
    this.minPrice = null;
    this.maxPrice = null;
    this.records = [];
    this.searched = false;
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.executeSearch();
  }

  
  openEdit(record: PricingRecord): void {
    this.editRecord = { ...record };
    this.showEditDialog = true;
  }

  onEditSave(updated: PricingRecord): void {
    this.pricingService.update(updated.id, updated).subscribe({
      next: (saved) => {
        const idx = this.records.findIndex(r => r.id === saved.id);
        if (idx >= 0) this.records[idx] = saved;
        this.showEditDialog = false;
        this.editRecord = null;
      },
      error: (err) => {
        alert('Update failed: ' + (err.error?.message || 'Unknown error'));
      }
    });
  }

  onEditCancel(): void {
    this.showEditDialog = false;
    this.editRecord = null;
  }

  
  openDelete(record: PricingRecord): void {
    this.deleteRecord = record;
    this.showDeleteDialog = true;
  }

  onDeleteConfirm(): void {
    if (!this.deleteRecord) return;
    this.pricingService.delete(this.deleteRecord.id).subscribe({
      next: () => {
        this.records = this.records.filter(r => r.id !== this.deleteRecord!.id);
        this.totalElements--;
        this.showDeleteDialog = false;
        this.deleteRecord = null;
      },
      error: (err) => {
        alert('Delete failed: ' + (err.error?.message || 'Unknown error'));
      }
    });
  }

  onDeleteCancel(): void {
    this.showDeleteDialog = false;
    this.deleteRecord = null;
  }
}