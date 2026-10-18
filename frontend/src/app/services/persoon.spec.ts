import { TestBed } from '@angular/core/testing';

import { Persoon } from './persoon';

describe('Persoon', () => {
  let service: Persoon;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Persoon);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
