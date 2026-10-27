import { ComponentFixture, TestBed } from "@angular/core/testing";
import { throwError, Subject } from "rxjs";

import { RegistreerPersoon } from "./registreer-persoon";
import { ErrorDetails, PersoonService } from "../../services/persoon.service";
import { PersoonServiceMock } from "../../services/testing/persoon.service.mock";

describe("RegistreerPersoon", () => {
  let component: RegistreerPersoon;
  let fixture: ComponentFixture<RegistreerPersoon>;
  let serviceMock: PersoonServiceMock;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistreerPersoon],
      providers: [{ provide: PersoonService, useClass: PersoonServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(RegistreerPersoon);
    component = fixture.componentInstance;
    await fixture.whenStable();

    serviceMock = TestBed.inject(PersoonService) as any;
    vi.useFakeTimers();
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("Moet een registratieformulier tonen met invoervelden voor SKN en BSN, en een verzendknop.", () => {
    expect(fixture.nativeElement.querySelector("input[name='skn']")).toBeTruthy();
    expect(fixture.nativeElement.querySelector("input[name='bsn']")).toBeTruthy();
    expect(fixture.nativeElement.querySelector("button[type='submit']")).toBeTruthy();
  });

  it("Als de gebruiker geldige waarden invult voor skn en bsn en het formulier verzendt, moet PersoonService.registreer() worden aangeroepen.", () => {
    const form = fixture.nativeElement.querySelector("form") as HTMLFormElement;
    const sknInput = fixture.nativeElement.querySelector("input[name='skn']") as HTMLInputElement;
    const bsnInput = fixture.nativeElement.querySelector("input[name='bsn']") as HTMLInputElement;

    sknInput.value = "SKN-2026-123456";
    bsnInput.value = "123456782";
    sknInput.dispatchEvent(new Event("input"));
    bsnInput.dispatchEvent(new Event("input"));
    form.dispatchEvent(new SubmitEvent("submit"));
    fixture.detectChanges();

    expect(serviceMock.registreer).toHaveBeenCalledWith({
      skn: "SKN-2026-123456",
      bsn: "123456782",
    });
  });

  it("Als de gebruiker ongeldige waarden invult voor skn en/of bsn en het formulier verzendt, moet PersoonService.registreer() NIET worden aangeroepen", () => {
    const form = fixture.nativeElement.querySelector("form") as HTMLFormElement;
    const sknInput = fixture.nativeElement.querySelector("input[name='skn']") as HTMLInputElement;
    const bsnInput = fixture.nativeElement.querySelector("input[name='bsn']") as HTMLInputElement;

    sknInput.value = "SKN-2026-123456";
    bsnInput.value = "111111111";
    sknInput.dispatchEvent(new Event("input"));
    bsnInput.dispatchEvent(new Event("input"));
    form.dispatchEvent(new SubmitEvent("submit"));
    fixture.detectChanges();

    expect(serviceMock.registreer).not.toHaveBeenCalled();
  });

  it("Als de gebruiker geldige waarden invult voor skn en bsn en het formulier verzendt dan krijgt de gebruiker een bijpassende melding te zien én worden de invoervelden geleegd.", () => {
    const form = fixture.nativeElement.querySelector("form") as HTMLFormElement;
    const sknInput = fixture.nativeElement.querySelector("input[name='skn']") as HTMLInputElement;
    const bsnInput = fixture.nativeElement.querySelector("input[name='bsn']") as HTMLInputElement;
    let successText = fixture.nativeElement.querySelector(".success-message");
    expect(component.isSent).toBe(false);
    expect(successText).toBeNull();

    sknInput.value = "SKN-2026-123456";
    bsnInput.value = "111111110";
    sknInput.dispatchEvent(new Event("input"));
    bsnInput.dispatchEvent(new Event("input"));
    form.dispatchEvent(new SubmitEvent("submit"));
    fixture.detectChanges();
    successText = fixture.nativeElement.querySelector(".success-message");

    expect(component.isSent).toBe(true);
    expect(successText).not.toBeNull();
    expect(successText.textContent).toContain("Persoon succesvol geregistreerd!");
    expect(sknInput.value).toBe("");
    expect(bsnInput.value).toBe("");
  });

  it("Als de gebruiker ongeldige waarden invult voor skn en het formulier probeert te verzenden dan krijgt de gebruiker een bijpassende melding te zien.", () => {
    const form = fixture.nativeElement.querySelector("form") as HTMLFormElement;
    const sknInput = fixture.nativeElement.querySelector("input[name='skn']") as HTMLInputElement;
    const bsnInput = fixture.nativeElement.querySelector("input[name='bsn']") as HTMLInputElement;
    let errorText = fixture.nativeElement.querySelector("#bsn-error-message");
    expect(component.sknControl.untouched).toBe(true);
    expect(errorText).toBeNull();

    sknInput.value = "SKN2026123456";
    bsnInput.value = "111111110";
    sknInput.dispatchEvent(new Event("input"));
    sknInput.dispatchEvent(new Event("blur"));
    bsnInput.dispatchEvent(new Event("input"));
    bsnInput.dispatchEvent(new Event("blur"));
    form.dispatchEvent(new SubmitEvent("submit"));
    fixture.detectChanges();
    errorText = fixture.nativeElement.querySelector("#skn-error-message");

    expect(component.sknControl.invalid).toBe(true);
    expect(errorText).not.toBeNull();
    expect(errorText.textContent).toContain("Ongeldige invoer voor strafrechtketennummer.");
  });

  it("Als de gebruiker ongeldige waarden invult voor bsn en het formulier probeert te verzenden dan krijgt de gebruiker een bijpassende melding te zien.", () => {
    const form = fixture.nativeElement.querySelector("form") as HTMLFormElement;
    const sknInput = fixture.nativeElement.querySelector("input[name='skn']") as HTMLInputElement;
    const bsnInput = fixture.nativeElement.querySelector("input[name='bsn']") as HTMLInputElement;
    let errorText = fixture.nativeElement.querySelector("#bsn-error-message");
    expect(component.bsnControl.untouched).toBe(true);
    expect(errorText).toBeNull();

    sknInput.value = "SKN-2026-123456";
    bsnInput.value = "111111111";
    sknInput.dispatchEvent(new Event("input"));
    sknInput.dispatchEvent(new Event("blur"));
    bsnInput.dispatchEvent(new Event("input"));
    bsnInput.dispatchEvent(new Event("blur"));
    form.dispatchEvent(new SubmitEvent("submit"));
    fixture.detectChanges();
    errorText = fixture.nativeElement.querySelector("#bsn-error-message");

    expect(component.bsnControl.invalid).toBe(true);
    expect(errorText).not.toBeNull();
    expect(errorText.textContent).toContain("Ongeldige invoer voor burgerservicenummer.");
  });

  it("Als de gebruiker een persoon probeert te registreren met al bestaande skn dan krijgt de gebruiker een bijpassende melding te zien.", () => {
    const form = fixture.nativeElement.querySelector("form") as HTMLFormElement;
    const sknInput = fixture.nativeElement.querySelector("input[name='skn']") as HTMLInputElement;
    const bsnInput = fixture.nativeElement.querySelector("input[name='bsn']") as HTMLInputElement;
    let errorText = fixture.nativeElement.querySelector("#skn-error-message");
    sknInput.value = "SKN-2026-123456";
    bsnInput.value = "111111110";
    const mockError: ErrorDetails = {
      isNetworkError: false,
      message: `Strafrechtketennummer ${sknInput.value} bestaat al.`,
      statusCode: 409,
      key: "skn-already-exists",
    };
    serviceMock.registreer.mockReturnValueOnce(throwError(() => mockError));
    expect(errorText).toBeNull();

    sknInput.dispatchEvent(new Event("input"));
    bsnInput.dispatchEvent(new Event("input"));
    form.dispatchEvent(new SubmitEvent("submit"));
    fixture.detectChanges();
    errorText = fixture.nativeElement.querySelector("#skn-error-message");

    expect(component.apiError?.key).toEqual("skn-already-exists");
    expect(errorText).not.toBeNull();
    expect(errorText.textContent).toContain("Strafrechtketennummer SKN-2026-123456 bestaat al.");
  });

  it("Als de gebruiker een persoon probeert te registreren met al bestaande bsn dan krijgt de gebruiker een bijpassende melding te zien.", () => {
    const form = fixture.nativeElement.querySelector("form") as HTMLFormElement;
    const sknInput = fixture.nativeElement.querySelector("input[name='skn']") as HTMLInputElement;
    const bsnInput = fixture.nativeElement.querySelector("input[name='bsn']") as HTMLInputElement;
    let errorText = fixture.nativeElement.querySelector("#bsn-error-message");
    sknInput.value = "SKN-2026-123456";
    bsnInput.value = "111111110";
    const mockError: ErrorDetails = {
      isNetworkError: false,
      message: `Burgerservicenummer ${bsnInput.value} bestaat al.`,
      statusCode: 409,
      key: "bsn-already-exists",
    };
    serviceMock.registreer.mockReturnValueOnce(throwError(() => mockError));
    expect(errorText).toBeNull();

    sknInput.dispatchEvent(new Event("input"));
    bsnInput.dispatchEvent(new Event("input"));
    form.dispatchEvent(new SubmitEvent("submit"));
    fixture.detectChanges();
    errorText = fixture.nativeElement.querySelector("#bsn-error-message");

    expect(component.apiError?.key).toEqual("bsn-already-exists");
    expect(errorText).not.toBeNull();
    expect(errorText.textContent).toContain("Burgerservicenummer 111111110 bestaat al.");
  });

  it("Als de gebruiker het formulier probeert te verzenden tijdens dat er nog een verzoek bezig is, moet dat worden tegengehouden.", () => {
    const openStream = new Subject<any>();
    const mockEvent = new Event("submit");
    serviceMock.registreer.mockReturnValueOnce(openStream);
    component.registreerPersoonForm.setValue({ skn: "SKN-2026-123456", bsn: "123456782" });

    component.submit(mockEvent);
    expect(component.isRegistering).toBe(true);
    component.submit(mockEvent);
    openStream.next({});
    openStream.complete();

    expect(component["service"].registreer).toHaveBeenCalledTimes(1);
    expect(component.isRegistering).toBe(false);
  });

  it("Als de gebruiker na het versturen snel wegnavigeert, moet de stream worden afgebroken.", () => {
    const openStream = new Subject<any>();
    serviceMock.registreer.mockReturnValueOnce(openStream);
    component.registreerPersoonForm.setValue({ skn: "SKN-2026-123456", bsn: "123456782" });

    component.submit(new Event("submit"));
    expect(openStream.observed).toBe(true);
    fixture.destroy();

    expect(openStream.observed).toBe(false);
  });
});
