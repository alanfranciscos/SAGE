import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateNurseModalComponent } from './update-nurse-modal.component';

describe('UpdateNurseModalComponent', () => {
  let component: UpdateNurseModalComponent;
  let fixture: ComponentFixture<UpdateNurseModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateNurseModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateNurseModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
