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

  // async getResidents() {
  //   const response = await this.api.get<ResidentListResponseDto>(
  //     'api/v1/resident'
  //   );

  //   if (response.status != 200) {
  //     throw new Error('Failed to fetch residents');
  //   }

  //   return response.data;
  // }

//    async getResidents(limit: number, skip: number, search?: string): Promise<ResidentListResponseDto> {
//   const response = await this.api.get(`http://localhost:8080/api/v1/resident?limit=${limit}&skip=${skip}&search=${search}`);
//   if (response.status != 200) {
//       throw new Error('Failed to fetch residents');
//     }

//     return response.data;
// }
 async getResidents(limit: number, skip: number, search?: string): Promise<ResidentListResponseDto> {
  const params = new URLSearchParams();
  params.set('limit', limit.toString());
  params.set('skip', skip.toString());

  if (search && search.trim() !== '') {
    params.set('search', search.trim());
  }

  const url = `http://localhost:8080/api/v1/resident?${params.toString()}`;
  const response = await this.api.get(url);

  if (response.status !== 200) {
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
  async updateResident(residentId: string, updateResidentRequestDto: CreateResidentRequestDto): Promise<void> {
  const response = await this.api.put(
    `api/v1/resident/${residentId}`,
    updateResidentRequestDto
  );

  if (response.status != 200) {
    throw new Error('Failed to update resident');
  }
}
}
