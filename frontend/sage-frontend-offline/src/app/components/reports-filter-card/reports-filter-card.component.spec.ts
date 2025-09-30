import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportsFilterCardComponent } from './reports-filter-card.component';

describe('ReportsFilterCardComponent', () => {
  let component: ReportsFilterCardComponent;
  let fixture: ComponentFixture<ReportsFilterCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportsFilterCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportsFilterCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
