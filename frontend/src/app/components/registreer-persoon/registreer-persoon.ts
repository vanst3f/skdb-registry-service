import { Component, inject, DestroyRef } from "@angular/core";
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from "@angular/forms";

import { moduloEleven } from "../../shared/validators/elf-proef.validator";
import { PersoonService, NatuurlijkPersoon, ErrorDetails } from "../../services/persoon.service";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

@Component({
  selector: "app-registreer-persoon",
  imports: [ReactiveFormsModule],
  templateUrl: "./registreer-persoon.html",
  styleUrl: "./registreer-persoon.css",
})
export class RegistreerPersoon {
  private service = inject(PersoonService);
  private destroyRef = inject(DestroyRef);

  public registreerPersoonForm = new FormGroup({
    skn: new FormControl("", [Validators.required, Validators.pattern(/^SKN-\d{4}-\d{6}$/)]),
    bsn: new FormControl("", [
      Validators.required,
      Validators.pattern(/^(?!123456789$)(?!000000000$)\d{9}$/),
      moduloEleven,
    ]),
  });

  public isRegistering = false;
  public isSent = false;
  public apiError: ErrorDetails | null = null;

  get sknControl() {
    return this.registreerPersoonForm.get("skn") as FormControl;
  }

  get bsnControl() {
    return this.registreerPersoonForm.get("bsn") as FormControl;
  }

  submit(event: Event) {
    event.preventDefault();

    if (this.isRegistering || this.registreerPersoonForm.invalid) {
      return;
    }

    this.isRegistering = true;

    const persoon: NatuurlijkPersoon = {
      skn: this.sknControl.value!,
      bsn: this.bsnControl.value!,
    };

    this.service
      .registreer(persoon)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.isRegistering = false;
          this.isSent = true;
          this.apiError = null;
          this.registreerPersoonForm.reset();
        },
        error: (error: ErrorDetails) => {
          this.isRegistering = false;
          this.apiError = error;
        },
      });
  }
}
