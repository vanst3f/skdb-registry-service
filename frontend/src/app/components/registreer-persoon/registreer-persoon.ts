import { Component, inject } from "@angular/core";
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from "@angular/forms";

import { moduloEleven } from "../../shared/validators/elf-proef.validator";
import { PersoonService, NatuurlijkPersoon } from "../../services/persoon.service";

@Component({
  selector: "app-registreer-persoon",
  imports: [ReactiveFormsModule],
  templateUrl: "./registreer-persoon.html",
  styleUrl: "./registreer-persoon.css",
})
export class RegistreerPersoon {
  private service = inject(PersoonService);

  public registreerPersoonForm = new FormGroup({
    skn: new FormControl("", [Validators.required, Validators.pattern(/^SKN-\d{4}-\d{6}$/)]),
    bsn: new FormControl("", [
      Validators.required,
      Validators.pattern(/^(?!123456789$)(?!000000000$)\d{9}$/),
      moduloEleven,
    ]),
  });

  public isSent = false;
  public errorMessage = ""; // hier doe ik nog niks mee

  get sknControl() {
    return this.registreerPersoonForm.get("skn") as FormControl;
  }

  get bsnControl() {
    return this.registreerPersoonForm.get("bsn") as FormControl;
  }

  submit(event: Event) {
    event.preventDefault();

    if (this.registreerPersoonForm.invalid) {
      return;
    }

    const persoon: NatuurlijkPersoon = {
      skn: this.sknControl.value!,
      bsn: this.bsnControl.value!,
    };

    this.service.registreer(persoon).subscribe({
      next: () => {
        this.isSent = true;
        this.registreerPersoonForm.reset();
      },
      error: () => {},
    });
  }
}
