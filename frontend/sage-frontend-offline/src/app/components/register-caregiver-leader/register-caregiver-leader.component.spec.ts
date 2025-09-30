import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterCaregiverLeaderComponent } from './register-caregiver-leader.component';

describe('RegisterCaregiverLeaderComponent', () => {
  let component: RegisterCaregiverLeaderComponent;
  let fixture: ComponentFixture<RegisterCaregiverLeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterCaregiverLeaderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterCaregiverLeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
