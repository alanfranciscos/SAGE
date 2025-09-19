import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardResidentCardComponent } from './dashboard-resident-card.component';

describe('DashboardResidentCardComponent', () => {
  let component: DashboardResidentCardComponent;
  let fixture: ComponentFixture<DashboardResidentCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardResidentCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardResidentCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
