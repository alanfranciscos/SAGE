// assist.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AssistService {
  private baseUrl = 'http://localhost:8080/api/v1/assist';

  constructor(private http: HttpClient) {}

  startAssist(assistId: string, caregiverToken: string): Observable<any> {
    return this.http.patch(`${this.baseUrl}/${assistId}/start`, {
      caregiverToken,
    });
  }
  finishAssist(
    assistId: string,
    caregiverToken: string,
    details: string
  ): Observable<any> {
    return this.http.patch(`${this.baseUrl}/${assistId}/finish`, {
      caregiverToken,
      details,
    });
  }
  // assist.service.ts
  getPendingAssists(limit: number, skip: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/pending?limit=${limit}&skip=${skip}`);
  }

  getFinishedAssists(limit: number, skip: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/history?limit=${limit}&skip=${skip}`);
  }

  // ✅ Detalhes de um chamado específico
  getFinishedAssistById(assistId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/history/${assistId}`);
  }
}
