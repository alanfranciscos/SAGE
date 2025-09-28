import { Injectable } from '@angular/core';
import { ApiService } from '../../services/api/api.service';

export interface Assist {
  observations: string;
  assistId: string;
  fullName: string;
  residentialUnit: string;
  elapsedTime: string;
  severityLevel: 'EMERGENCY' | 'WARNING' | string;
  status: 'pending' | 'in_attendance' | string;
}

export interface AssistResponse {
  data: Assist[];
  total: number;
  limit: number;
  skip: number;
}

@Injectable({
  providedIn: 'root',
})
export class AssistService {
  api: ReturnType<ApiService['getApi']>;

  constructor(private apiService: ApiService) {
    this.api = this.apiService.getApi();
  }

  /** Retorna a lista de assistências pendentes */
  async getPendingAssists(limit = 10, skip = 0): Promise<AssistResponse> {
    const response = await this.api.get<AssistResponse>(
      `http://localhost:8080/api/v1/assist/pending?limit=${limit}&skip=${skip}`
    );

    if (response.status !== 200) {
      throw new Error('Failed to fetch pending assists');
    }

    return response.data;
  }

  /** Filtra apenas as assistências de emergência */
  async getEmergencyAssists(limit = 10, skip = 0): Promise<Assist[]> {
    const pending = await this.getPendingAssists(limit, skip);
    return pending.data.filter(
      (assist) => assist.severityLevel === 'EMERGENCY'
    );
  }

  /** Filtra apenas as assistências de aviso (warning) */
  async getWarningAssists(limit = 10, skip = 0): Promise<Assist[]> {
    const pending = await this.getPendingAssists(limit, skip);
    return pending.data.filter((assist) => assist.severityLevel === 'WARNING');
  }
}
