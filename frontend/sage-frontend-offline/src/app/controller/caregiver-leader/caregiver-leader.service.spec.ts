import { TestBed } from '@angular/core/testing';
import { CaregiverLeaderService } from './caregiver-leader.service';


describe('CaregiverLeaderService', () => {
  let service: CaregiverLeaderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CaregiverLeaderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
