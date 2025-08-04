import { Injectable } from '@angular/core';
import { ApiService } from '../../services/api/api.service';
import {
  ResidentListResponseDto,
  CreateResidentRequestDto,
  ResidentDetailsResponseDto,
} from '../../model/Resident';

@Injectable({
  providedIn: 'root',
})
export class ResidentService {
  api: ReturnType<ApiService['getApi']>;
  constructor(private apiService: ApiService) {
    this.api = this.apiService.getApi();
  }

  async getResidents() {
    const response = await this.api.get<ResidentListResponseDto>(
      'api/v1/resident'
    );

    if (response.status != 200) {
      throw new Error('Failed to fetch residents');
    }

    return response.data;
  }

  async getResidentById(residentId: string) {
    const response = await this.api.get<ResidentDetailsResponseDto>(
      `api/v1/resident/${residentId}`
    );

    if (response.status != 200) {
      throw new Error('Failed to fetch resident details');
    }

    return response.data;
  }

  async createResident(
    createResidentRequestDto: CreateResidentRequestDto
  ): Promise<string> {
    const response = await this.api.post(
      'api/v1/resident',
      createResidentRequestDto
    );

    if (response.status != 201) {
      throw new Error('Failed to create resident');
    }

    return response.data?.id ?? 'Resident created successfully';
  }
}
