import { Injectable } from '@angular/core';
import { CreateCaregiverLeaderRequestDto } from '../../model/CaregiverLeader';
import { ApiService } from '../../services/api/api.service';

@Injectable({
  providedIn: 'root'
})
export class CaregiverLeaderService {
  api: ReturnType<ApiService['getApi']>;

  constructor(private apiService: ApiService) {
    this.api = this.apiService.getApi();
  }

  async createCaregiverLeader(
    createCaregiverLeaderRequestDto: CreateCaregiverLeaderRequestDto
  ): Promise<string> {
    const response = await this.api.post(
      'api/v1/resident',
      createCaregiverLeaderRequestDto
    );

    if (response.status != 201) {
      throw new Error('Failed to create resident');
    }

    return response.data?.id ?? 'Resident created successfully';
  }

  async countAllActiveCaregiverLeader(
  ): Promise<number> {
    const response = await this.api.get(
      'api/v1/caregiver/count-caregiver-leader'
    );
    return response.data.total;
  }

}
