import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Persoon {
  skn: string;
  bsn: string;
}

@Injectable({
  providedIn: 'root',
})
export class PersoonService {
  private http = inject(HttpClient);

  private apiUrl = `http://localhost:8080/api/personen`;

  registreer(persoon: Persoon): Observable<void> {
    return this.http.post<void>(this.apiUrl, persoon);
  }

  getBySkn(skn: Persoon['skn']): Observable<Persoon> {
    return this.http.get<Persoon>(`${this.apiUrl}/${skn}`);
  }

  getByBsn(bsn: Persoon['bsn']): Observable<Persoon> {
    return this.http.get<Persoon>(`${this.apiUrl}?bsn=${bsn}`);
  }
}
