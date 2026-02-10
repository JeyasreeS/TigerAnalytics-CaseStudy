import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PricingSearch } from './pricing-search';

describe('PricingSearch', () => {
  let component: PricingSearch;
  let fixture: ComponentFixture<PricingSearch>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PricingSearch]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PricingSearch);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
