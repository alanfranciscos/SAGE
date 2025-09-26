import { Injectable } from '@angular/core';
import { Nurse } from '../model/Nurse';
import { ApiService } from '../services/api/api.service';

@Injectable({
  providedIn: 'root',
})
export class NurseService {
  api: ReturnType<ApiService['getApi']>;
  constructor(private apiService: ApiService) {
    this.api = this.apiService.getApi();
  }

  async getNurses(
    limit: number,
    skip: number,
    search?: string
  ): Promise<Nurse[]> {
    const queryParams = new URLSearchParams({
      limit: limit.toString(),
      skip: skip.toString(),
    });

    if (search) {
      queryParams.append('search', search);
    }

    const response = await this.api.get<any[]>(
      `http://localhost:8080/api/v1/caregiver?${queryParams.toString()}`
    );

    if (response.status !== 200) {
      throw new Error('Failed to fetch nurses');
    }

    // 🔄 mapeia os campos
    return response.data.map((item) => ({
      id: item.cpf, // ou outro identificador único
      name: item.fullName,
      cpf: item.cpf,
      email: '', // backend não retorna
      tel: '', // backend não retorna
      token: item.token,
      status: item.active ? 'active' : 'inactive',
      lastUse: item.lastUsedToken ?? 'Nunca usado',
      registration: '', // backend não retorna
    }));
  }
}
