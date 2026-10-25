import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";

export interface NatuurlijkPersoon {
  skn: string;
  bsn: string;
}

@Injectable({
  providedIn: "root",
})
export class PersoonService {
  private http = inject(HttpClient);

  private apiUrl = `http://localhost:8080/api/personen`;

  registreer(persoon: NatuurlijkPersoon): Observable<void> {
    return this.http.post<void>(this.apiUrl, persoon);
  }

  getBySkn(skn: NatuurlijkPersoon["skn"]): Observable<NatuurlijkPersoon> {
    return this.http.get<NatuurlijkPersoon>(`${this.apiUrl}/${skn}`);
  }

  getByBsn(bsn: NatuurlijkPersoon["bsn"]): Observable<NatuurlijkPersoon> {
    return this.http.get<NatuurlijkPersoon>(`${this.apiUrl}?bsn=${bsn}`);
  }
}
