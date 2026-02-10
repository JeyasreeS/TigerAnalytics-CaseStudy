import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PricingRecord } from '../../models/pricing.model';

@Component({
  selector: 'app-pricing-edit-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pricing-edit-dialog.html',
  styleUrl: './pricing-edit-dialog.css'
})
export class PricingEditDialog {
  @Input() record!: PricingRecord;
  @Output() save = new EventEmitter<PricingRecord>();
  @Output() cancel = new EventEmitter<void>();

  onSave(): void {
    this.save.emit(this.record);
  }

  onCancel(): void {
    this.cancel.emit();
  }
}