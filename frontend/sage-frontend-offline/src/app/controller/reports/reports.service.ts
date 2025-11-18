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

// Tipos para responses dinâmicos
interface TopFiveCaregiverResponse {
  [name: string]: { quantity: number; meanResponse: string };
}

interface TopFiveResidentsResponse {
  [name: string]: { quantity: number; meanResponse: string | null };
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
  async getTotalAssists(startDate?: string, endDate?: string): Promise<number> {
    const params: Record<string, string> = {};
    if (startDate) params['startDate'] = startDate;
    if (endDate) params['endDate'] = endDate;

    const response = await this.api.get<{ totalAssists: number }>(
      '/api/v1/reports/total-assists',
      { params }
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
    const response = await this.api.get<TopFiveCaregiverResponse>(
      '/api/v1/reports/top-five/caregiver-performance'
    );

    if (response.status !== 200)
      throw new Error('Failed to fetch top-five caregiver performance');

    // Ensure TypeScript knows the shape of response.data
    const dataMap = response.data as TopFiveCaregiverResponse;

    return Object.entries(dataMap).map(([name, data]) => ({
      name,
      quantity: data.quantity,
      meanResponse: data.meanResponse,
    }));
  }

  /** Ranking top 5 residentes */
  async getTopFiveCallResidents(): Promise<ResidentPerformance[]> {
    const response = await this.api.get<TopFiveResidentsResponse>(
      '/api/v1/reports/top-five/call-residents'
    );

    if (response.status !== 200)
      throw new Error('Failed to fetch top-five call residents');

    // Ensure TypeScript knows the shape of response.data
    const dataMap = response.data as TopFiveResidentsResponse;

    return Object.entries(dataMap).map(([name, data]) => ({
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
  async getAverageResponseTime(
    startDate?: string,
    endDate?: string
  ): Promise<string> {
    const params: Record<string, string> = {};
    if (startDate) params['startDate'] = startDate;
    if (endDate) params['endDate'] = endDate;

    const response = await this.api.get<{ averageResponseTime: string }>(
      '/api/v1/reports/average-response-time',
      { params }
    );

    if (response.status !== 200)
      throw new Error('Failed to fetch average response time');

    return response.data.averageResponseTime;
  }

  /** Tempo médio de resolução */
  async getAverageResolutionTime(
    startDate?: string,
    endDate?: string
  ): Promise<string> {
    const params: Record<string, string> = {};
    if (startDate) params['startDate'] = startDate;
    if (endDate) params['endDate'] = endDate;

    const response = await this.api.get<{ averageResolutionTime: string }>(
      '/api/v1/reports/average-resolution-time',
      { params }
    );

    if (response.status !== 200)
      throw new Error('Failed to fetch average resolution time');

    return response.data.averageResolutionTime;
  }
}
