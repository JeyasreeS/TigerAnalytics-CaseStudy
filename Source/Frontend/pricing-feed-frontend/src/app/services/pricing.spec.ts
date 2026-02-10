import { TestBed } from '@angular/core/testing';

import { PricingService } from './pricing';

describe('Pricing', () => {
  let service: PricingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PricingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
