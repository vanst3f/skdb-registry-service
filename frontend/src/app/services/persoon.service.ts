import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { catchError, Observable, throwError } from "rxjs";

export interface NatuurlijkPersoon {
  skn: string;
  bsn: string;
}

type ProblemDetails = {
  type: string;
  title: string;
  status: number;
  detail: string;
  instance: string;
  errorKey: string;
};

export type ErrorDetails = {
  isNetworkError: boolean;
  message: string;
  statusCode?: number;
  key?: string;
};

@Injectable({
  providedIn: "root",
})
export class PersoonService {
  private http = inject(HttpClient);

  private apiUrl = `http://localhost:8080/api/personen`;

  public registreer(persoon: NatuurlijkPersoon): Observable<NatuurlijkPersoon> {
    return this.http
      .post<NatuurlijkPersoon>(this.apiUrl, persoon)
      .pipe(catchError(this.handleError));
  }

  public getBySkn(skn: NatuurlijkPersoon["skn"]): Observable<NatuurlijkPersoon> {
    return this.http.get<NatuurlijkPersoon>(`${this.apiUrl}/${skn}`);
  }

  public getByBsn(bsn: NatuurlijkPersoon["bsn"]): Observable<NatuurlijkPersoon> {
    return this.http.get<NatuurlijkPersoon>(`${this.apiUrl}?bsn=${bsn}`);
  }

  private handleError(error: HttpErrorResponse) {
    const errorDetails: ErrorDetails = {
      isNetworkError: error.status === 0,
      message: "Er is een onbekende fout opgetreden.",
    };

    if (error.status === 0) {
      errorDetails.message = "De server is onbereikbaar. Controleer je internetverbinding.";
    } else if (error.status === 500) {
      errorDetails.message = "Er is een interne serverfout opgetreden.";
    } else {
      const backendError = error.error as ProblemDetails;
      errorDetails.message = backendError.detail;
      errorDetails.statusCode = backendError.status;
      errorDetails.key = backendError.errorKey;
    }
    return throwError(() => error);
  }
}
