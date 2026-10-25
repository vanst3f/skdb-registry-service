import { AbstractControl, ValidationErrors } from "@angular/forms";

export function moduloEleven(control: AbstractControl): ValidationErrors | null {
  const value = control.value;

  if (!value) return null;

  const isNegenCijfers = /^\d{9}$/.test(value);

  let num: number = 0;

  for (let i = 0; i < value.length - 1; i++) {
    num += Number(value[i]) * (9 - i);
  }
  num += value[8] * -1;

  if (!(num > 0) || !(num % 11 === 0)) {
    return { moduloCheck: false };
  }

  return null;
}
