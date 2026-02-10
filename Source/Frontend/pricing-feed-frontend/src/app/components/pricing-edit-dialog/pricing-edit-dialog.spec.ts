import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PricingEditDialog } from './pricing-edit-dialog';

describe('PricingEditDialog', () => {
  let component: PricingEditDialog;
  let fixture: ComponentFixture<PricingEditDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PricingEditDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PricingEditDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
