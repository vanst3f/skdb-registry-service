import { ComponentFixture, fakeAsync, TestBed, tick } from "@angular/core/testing";

import { RegistreerPersoon } from "./registreer-persoon";
import { PersoonService } from "../../services/persoon.service";
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
});
