import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StartAssistDialogComponent } from './start-assist-dialog.component';

describe('StartAssistDialogComponent', () => {
  let component: StartAssistDialogComponent;
  let fixture: ComponentFixture<StartAssistDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StartAssistDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StartAssistDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
