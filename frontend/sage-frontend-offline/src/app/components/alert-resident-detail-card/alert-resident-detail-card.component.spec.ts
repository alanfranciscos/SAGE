import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertResidentDetailCardComponent } from './alert-resident-detail-card.component';

describe('AlertResidentDetailCardComponent', () => {
  let component: AlertResidentDetailCardComponent;
  let fixture: ComponentFixture<AlertResidentDetailCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertResidentDetailCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlertResidentDetailCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
