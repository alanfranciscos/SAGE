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
      'api/auth/register',
      createCaregiverLeaderRequestDto
    );

    if (response.status !== 200) { 
      throw new Error('Falha na resposta do servidor.');
    }
    return response.data?.token || 'Registro realizado com sucesso';
}

  async countAllActiveCaregiverLeader(
  ): Promise<number> {
    const response = await this.api.get(
      'api/v1/caregiver/count-caregiver-leader'
    );
    return response.data.total;
  }

    async findAllCaregiverLeader(
  ): Promise<string> {
    const response = await this.api.get(
      'api/v1/resident'
    );

    if (response.status != 201) {
      throw new Error('Failed to create resident');
    }

    return response.data?.id ?? 'Resident created successfully';
  }

}
