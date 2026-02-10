import { Routes } from '@angular/router';
import { CsvUpload } from './components/csv-upload/csv-upload';
import { PricingSearch } from './components/pricing-search/pricing-search';

export const routes: Routes = [
  { path: '', redirectTo: 'upload', pathMatch: 'full' },
  { path: 'upload', component: CsvUpload },
  { path: 'search', component: PricingSearch },
  { path: '**', redirectTo: 'upload' }
];