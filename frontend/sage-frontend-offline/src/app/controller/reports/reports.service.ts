import { Injectable } from '@angular/core';
import { ApiService } from '../../services/api/api.service';

export interface CaregiverPerformance {
  name: string;
  quantity: number;
  meanResponse: string;
}

export interface ResidentPerformance {
  name: string;
  quantity: number;
  meanResponse: string | null;
}

@Injectable({
  providedIn: 'root',
})
export class ReportsService {
  api: ReturnType<ApiService['getApi']>;

  constructor(private apiService: ApiService) {
    this.api = this.apiService.getApi();
  }

  /** Total de assistências */
  async getTotalAssists(): Promise<number> {
    const response = await this.api.get<{ totalAssists: number }>(
      '/api/v1/reports/total-assists'
    );
    if (response.status !== 200)
      throw new Error('Failed to fetch total assists');
    return response.data.totalAssists;
  }

  /** Taxa de assistências críticas */
  async getCriticalAssistsRate(): Promise<number> {
    const response = await this.api.get<{ criticalAssistsRate: number }>(
      '/api/v1/reports/critical-assists-rate'
    );
    if (response.status !== 200)
      throw new Error('Failed to fetch critical assists rate');
    return response.data.criticalAssistsRate;
  }

  /** Ranking top 5 cuidadores */
  async getTopFiveCaregiverPerformance(): Promise<CaregiverPerformance[]> {
    const response = await this.api.get<
      Record<string, { quantity: number; meanResponse: string }>
    >('/api/v1/reports/top-five/caregiver-performance');
    if (response.status !== 200)
      throw new Error('Failed to fetch top-five caregiver performance');
    return Object.entries(response.data).map(([name, data]) => ({
      name,
      quantity: data.quantity,
      meanResponse: data.meanResponse,
    }));
  }

  /** Ranking top 5 residentes */
  async getTopFiveCallResidents(): Promise<ResidentPerformance[]> {
    const response = await this.api.get<
      Record<string, { quantity: number; meanResponse: string | null }>
    >('/api/v1/reports/top-five/call-residents');
    if (response.status !== 200)
      throw new Error('Failed to fetch top-five call residents');
    return Object.entries(response.data).map(([name, data]) => ({
      name,
      quantity: data.quantity,
      meanResponse: data.meanResponse,
    }));
  }

  /** Chamados por dia da semana */
  async getCallsByWeekday(): Promise<Record<string, number>> {
    const response = await this.api.get<{
      weekdayAverage: Record<string, number>;
    }>('/api/v1/reports/calls/weekday');
    if (response.status !== 200)
      throw new Error('Failed to fetch calls by weekday');
    return response.data.weekdayAverage;
  }

  /** Chamados por hora do dia */
  async getCallsHourlyByDay(): Promise<Record<string, number>> {
    const response = await this.api.get<{
      hourlyAverage: Record<string, number>;
    }>('/api/v1/reports/calls/hourly-by-day');
    if (response.status !== 200)
      throw new Error('Failed to fetch calls hourly by day');
    return response.data.hourlyAverage;
  }

  /** Tempo médio de resposta */
  async getAverageResponseTime(): Promise<string> {
    const response = await this.api.get<{ averageResponseTime: string }>(
      '/api/v1/reports/average-response-time'
    );
    if (response.status !== 200)
      throw new Error('Failed to fetch average response time');
    return response.data.averageResponseTime;
  }

  /** Tempo médio de resolução */
  async getAverageResolutionTime(): Promise<string> {
    const response = await this.api.get<{ averageResolutionTime: string }>(
      '/api/v1/reports/average-resolution-time'
    );
    if (response.status !== 200)
      throw new Error('Failed to fetch average resolution time');
    return response.data.averageResolutionTime;
  }
}
