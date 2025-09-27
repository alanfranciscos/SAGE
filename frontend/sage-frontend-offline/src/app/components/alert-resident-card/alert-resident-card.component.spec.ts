import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertResidentCardComponent } from './alert-resident-card.component';

describe('AlertResidentCardComponent', () => {
  let component: AlertResidentCardComponent;
  let fixture: ComponentFixture<AlertResidentCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertResidentCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlertResidentCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
