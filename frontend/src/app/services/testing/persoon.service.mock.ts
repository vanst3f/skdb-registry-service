import { of } from "rxjs";
import { vi } from "vitest";

export class PersoonServiceMock {
  registreer = vi.fn(() => of(undefined));
}
