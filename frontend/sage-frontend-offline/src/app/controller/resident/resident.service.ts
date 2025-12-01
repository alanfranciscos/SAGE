import { Injectable } from '@angular/core';
import { ApiService } from '../../services/api/api.service';
import axios from 'axios';

import {
  // ResidentListResponseDto,
  CreateResidentRequestDto,
  ResidentDetailsResponseDto,
  Resident,
} from '../../model/Resident';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ResidentService {
  api: ReturnType<ApiService['getApi']>;
  private totalActiveCallsSubject = new BehaviorSubject<number>(0);
  totalActiveCalls$ = this.totalActiveCallsSubject.asObservable();

  constructor(private apiService: ApiService) {
    this.api = this.apiService.getApi();
  }

  async getResidents(
    limit: number,
    skip: number,
    search?: string,
    active?: boolean // opcional
  ): Promise<Resident[]> {
    const queryParams = new URLSearchParams({
      limit: limit.toString(),
      skip: skip.toString(),
    });

    if (search) queryParams.append('search', search);

    // só adiciona 'active' se não for undefined
    if (active !== undefined) {
      queryParams.append('active', String(active));
    }

    const response = await this.api.get<Resident[]>(
      `http://localhost:8080/api/v1/resident?${queryParams.toString()}`
    );

    if (response.status !== 200) throw new Error('Failed to fetch residents');

    return response.data;
  }

  async getTotalResidentsNumber(): Promise<number> {
    const response = await this.api.get<{ total: number }>(
      'api/v1/resident/card/total'
    );
    if (response.status !== 200) {
      throw new Error('Failed to fetch total number of residents');
    }
    return response.data.total;
  }

  // async getTotalActiveResidentsCalls(): Promise<number> {
  //   const response = await this.api.get<{ activeAlerts: number }>(
  //     'api/v1/resident/card/active-alerts'
  //   );
  //   if (response.status !== 200) {
  //     throw new Error('Failed to fetch total number of active alerts');
  //   }
  //   return response.data.activeAlerts;
  // }
  async getTotalActiveResidentsCalls(): Promise<number> {
    const response = await this.api.get<{ activeAlerts: number }>(
      'api/v1/resident/card/active-alerts'
    );
    if (response.status !== 200) {
      throw new Error('Failed to fetch total number of active alerts');
    }

    // 👈 Atualiza o BehaviorSubject
    this.totalActiveCallsSubject.next(response.data.activeAlerts);

    return response.data.activeAlerts;
  }

  // 👈 opcional: função para forçar atualização manual
  async refreshTotalActiveCalls(): Promise<number> {
    return await this.getTotalActiveResidentsCalls();
  }

  async getMeanTime(): Promise<string> {
    const response = await this.api.get<{ meanTimeAssist: string }>(
      'api/v1/resident/card/mean-time'
    );

    if (response.status !== 200) {
      throw new Error('Failed to fetch mean time');
    }

    // const rawTime = response.data.meanTimeAssist; // Ex: "02:41:15"
    // const [hoursStr, minutesStr, secondsStr] = rawTime.split(':');
    // const hours = parseInt(hoursStr, 10);
    // const minutes = parseInt(minutesStr, 10);
    // const seconds = parseInt(secondsStr, 10);

    // if (hours > 0) {
    //   return `${rawTime} h`;
    // } else if (minutes > 0) {
    //   return `${minutesStr}:${secondsStr} min`;
    // } else {
    //   return `${secondsStr} seg`;
    // }

    return response.data.meanTimeAssist;
  }

  async getTotalResolvedToday(): Promise<number> {
    const response = await this.api.get<{ solvedToday: number }>(
      'api/v1/resident/card/solved-today'
    );
    if (response.status !== 200) {
      throw new Error('Failed to fetch total number of residents');
    }
    // console.log('chamada da api:', response.data);
    return response.data.solvedToday;
  }

  async getResidentById(
    residentId: string
  ): Promise<ResidentDetailsResponseDto> {
    const response = await this.api.get<ResidentDetailsResponseDto>(
      `http://localhost:8080/api/v1/resident/${residentId}`
    );

    if (response.status !== 200) {
      throw new Error('Failed to fetch resident details');
    }

    return response.data;
  }

  // async createResident(
  //     createResidentRequestDto: CreateResidentRequestDto
  //   ): Promise<string> {
  //     const response = await this.api.post(
  //       'api/v1/resident',
  //       createResidentRequestDto
  //     );

  //     if (response.status != 201) {
  //       throw new Error('Failed to create resident');
  //     }

  //     return response.data?.id ?? 'Resident created successfully';
  //   }
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

    // Aqui pegamos a resposta diretamente, que já é o ID
    return response.data as string;
  }

  async updateResident(
    residentId: string,
    updateResidentRequestDto: CreateResidentRequestDto
  ): Promise<void> {
    const response = await this.api.put(
      `api/v1/resident/${residentId}`,
      updateResidentRequestDto
    );

    if (response.status != 200) {
      throw new Error('Failed to update resident');
    }
  }
  // async updateResidentImage(residentId: string, file: File): Promise<void> {
  //   const formData = new FormData();
  //   formData.append('imageData', file); // o nome deve ser igual ao esperado pelo backend

  //   const response = await this.api.patch(
  //     `http://localhost:8080/api/v1/resident/${residentId}/image`,
  //     formData,
  //     {
  //       headers: {
  //         'Content-Type': 'multipart/form-data', // necessário para envio de arquivos
  //       },
  //     }
  //   );

  //   if (response.status !== 200 && response.status !== 204) {
  //     throw new Error('Falha ao atualizar a imagem do residente');
  //   }
  // }
  async updateResidentImage(residentId: string, file: File): Promise<void> {
    const formData = new FormData();
    formData.append('imageData', file);

    const response = await axios.patch(
      `http://localhost:8080/api/v1/resident/${residentId}/image`,
      formData
    );

    if (response.status !== 200 && response.status !== 204) {
      throw new Error('Falha ao atualizar a imagem do residente');
    }
  }


  async uploadResidentImage(residentId: string, file: File): Promise<void> {
    const formData = new FormData();
    formData.append('imageData', file); // o nome deve ser igual ao esperado pelo backend

    try {
      const response = await axios.patch(
        `http://localhost:8080/api/v1/resident/${residentId}/image`,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        }
      );

      if (response.status !== 200) {
        throw new Error('Falha ao atualizar a imagem do residente');
      }
    } catch (error) {
      console.error('Erro no upload da imagem:', error);
      throw error;
    }
  }
  async deactivateResident(residentId: string): Promise<void> {
    const response = await this.api.patch(
      `http://localhost:8080/api/v1/resident/${residentId}/deactivate`
    );

    if (response.status !== 200 && response.status !== 204) {
      throw new Error('Falha ao desativar residente');
    }
  }

  async activateResident(residentId: string): Promise<void> {
    const response = await this.api.patch(
      `http://localhost:8080/api/v1/resident/${residentId}/activate`
    );

    if (response.status !== 200 && response.status !== 204) {
      throw new Error('Falha ao ativar residente');
    }
  }
}
