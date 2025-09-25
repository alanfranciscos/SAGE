import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterNurseComponentComponent } from './register-nurse-component.component';

describe('RegisterNurseComponentComponent', () => {
  let component: RegisterNurseComponentComponent;
  let fixture: ComponentFixture<RegisterNurseComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterNurseComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterNurseComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
