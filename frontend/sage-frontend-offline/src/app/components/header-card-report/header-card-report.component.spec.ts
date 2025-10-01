import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderCardReportComponent } from './header-card-report.component';

describe('HeaderCardReportComponent', () => {
  let component: HeaderCardReportComponent;
  let fixture: ComponentFixture<HeaderCardReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderCardReportComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderCardReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
