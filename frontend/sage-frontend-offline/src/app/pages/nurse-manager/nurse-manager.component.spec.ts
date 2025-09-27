import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseManagerComponent } from './nurse-manager.component';

describe('NurseManagerComponent', () => {
  let component: NurseManagerComponent;
  let fixture: ComponentFixture<NurseManagerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NurseManagerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NurseManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
