import { TestBed } from '@angular/core/testing';

import { AssistService } from './assist.service';

describe('AssistService', () => {
  let service: AssistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
