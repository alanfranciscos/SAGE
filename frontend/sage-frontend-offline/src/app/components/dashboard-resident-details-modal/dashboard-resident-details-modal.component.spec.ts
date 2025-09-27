import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardResidentDetailsModalComponent } from './dashboard-resident-details-modal.component';

describe('DashboardResidentDetailsModalComponent', () => {
  let component: DashboardResidentDetailsModalComponent;
  let fixture: ComponentFixture<DashboardResidentDetailsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardResidentDetailsModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardResidentDetailsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
