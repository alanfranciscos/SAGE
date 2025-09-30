import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { caregiverGuard } from './caregiver.guard';

describe('caregiverGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => caregiverGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
