import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenericReportsCardComponent } from './generic-reports-card.component';

describe('GenericReportsCardComponent', () => {
  let component: GenericReportsCardComponent;
  let fixture: ComponentFixture<GenericReportsCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GenericReportsCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GenericReportsCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
