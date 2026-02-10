import { ChangeDetectorRef, Component, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PricingService } from '../../services/pricing';
import { CsvUploadResponse } from '../../models/pricing.model';

@Component({
  selector: 'app-csv-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './csv-upload.html',
  styleUrl: './csv-upload.css'
})
export class CsvUpload {
  selectedFile: File | null = null;
  uploading = false;
  response: CsvUploadResponse | null = null;
  errorMessage: string | null = null;

  constructor(private pricingService: PricingService,
    private cdr: ChangeDetectorRef
  ) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      if (!file.name.endsWith('.csv')) {
        this.errorMessage = 'Please select a CSV file.';
        this.selectedFile = null;
        return;
      }
      this.selectedFile = file;
      this.errorMessage = null;
      this.response = null;
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    if (event.dataTransfer?.files && event.dataTransfer.files.length > 0) {
      const file = event.dataTransfer.files[0];
      if (!file.name.endsWith('.csv')) {
        this.errorMessage = 'Please select a CSV file.';
        return;
      }
      this.selectedFile = file;
      this.errorMessage = null;
      this.response = null;
    }
  }

  upload(): void {
    if (!this.selectedFile) return;
    this.uploading = true;
    this.errorMessage = null;
    this.response = null;

    this.pricingService.uploadCsv(this.selectedFile).subscribe({
      next: (res) => {
        this.response = res;
        this.uploading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Upload failed. Please try again.';
        this.uploading = false;
        this.cdr.detectChanges();   
      }
    });
  }

  reset(): void {
    this.selectedFile = null;
    this.response = null;
    this.errorMessage = null;
  }

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  triggerFileInput(): void {
    this.fileInput.nativeElement.click();
  }
}