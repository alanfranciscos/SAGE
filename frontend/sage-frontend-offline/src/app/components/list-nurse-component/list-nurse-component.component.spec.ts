import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListNurseComponentComponent } from './list-nurse-component.component';

describe('ListNurseComponentComponent', () => {
  let component: ListNurseComponentComponent;
  let fixture: ComponentFixture<ListNurseComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListNurseComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListNurseComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
