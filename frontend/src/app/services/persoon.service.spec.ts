import { TestBed } from "@angular/core/testing";

import { PersoonService } from "./persoon.service";
import { provideHttpClient } from "@angular/common/http";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";

describe("Persoon", () => {
  let service: PersoonService;
  let testController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(PersoonService);
    testController = TestBed.inject(HttpTestingController);
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });

  it("Moet een POST-request sturen naar /api/personen met juiste payload bij registratie.", () => {
    service.registreer({ skn: "SKN-2026-123456", bsn: "123456782" }).subscribe();

    const req = testController.expectOne({
      url: "http://localhost:8080/api/personen",
      method: "POST",
    });

    expect(req.request.body).toEqual({
      skn: "SKN-2026-123456",
      bsn: "123456782",
    });

    req.flush(null, {
      status: 201,
      statusText: "Created",
    });
  });

  it("Moet een GET-request sturen naar /api/personen/{skn} met juiste payload en response.", () => {
    service.getBySkn("SKN-2026-123456").subscribe((data) => {
      expect(data).toEqual({
        skn: "SKN-2026-123456",
        bsn: "123456782",
      });
    });

    const req = testController.expectOne({
      url: "http://localhost:8080/api/personen/SKN-2026-123456",
      method: "GET",
    });

    req.flush(
      {
        skn: "SKN-2026-123456",
        bsn: "123456782",
      },
      {
        status: 200,
        statusText: "Ok",
      },
    );
  });

  it("Moet een GET-request sturen naar /api/personen?bsn={bsn} met juiste payload en response.", () => {
    service.getByBsn("123456782").subscribe((data) => {
      expect(data).toEqual({
        skn: "SKN-2026-123456",
        bsn: "123456782",
      });
    });

    const req = testController.expectOne({
      url: "http://localhost:8080/api/personen?bsn=123456782",
      method: "GET",
    });

    req.flush(
      {
        skn: "SKN-2026-123456",
        bsn: "123456782",
      },
      {
        status: 200,
        statusText: "Ok",
      },
    );
  });

  afterEach(() => {
    testController.verify();
  });
});
