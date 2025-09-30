import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenericReportsRankingCardComponent } from './generic-reports-ranking-card.component';

describe('GenericReportsRankingCardComponent', () => {
  let component: GenericReportsRankingCardComponent;
  let fixture: ComponentFixture<GenericReportsRankingCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GenericReportsRankingCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GenericReportsRankingCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
