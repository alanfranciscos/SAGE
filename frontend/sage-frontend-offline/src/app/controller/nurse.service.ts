import { Injectable } from '@angular/core';
import { Nurse, RegisterNurseDto } from '../model/Nurse';
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
      id: item.id,
      name: item.fullName,
      cpf: item.cpf,
      email: item.email ?? '',
      tel: item.phone ?? '',
      token: item.token,
      status: item.active ? 'active' : 'inactive',
      lastUse: item.lastUsedToken ?? 'Nunca usado',
      registration: item.registration ?? '',
    }));
  }
  async registerNurse(nurse: RegisterNurseDto): Promise<void> {
    const response = await this.api.post('/api/v1/caregiver', nurse);

    if (response.status !== 201 && response.status !== 200) {
      throw new Error('Erro ao cadastrar enfermeira');
    }
  }
  async updateNurse(id: string, nursePayload: any): Promise<any> {
    const response = await this.api.put(
      `/api/v1/caregiver/${id}`,
      nursePayload
    );

    if (response.status !== 200 && response.status !== 204) {
      throw new Error('Erro ao atualizar enfermeira');
    }

    // ⚡ Retorna algo útil se precisar atualizar a lista
    return { id, ...nursePayload };
  }
  async setActiveStatus(id: string, active: boolean): Promise<void> {
    await this.api.patch(`/api/v1/caregiver/${id}/active`, { active });
    // não precisa verificar status, se houver erro, vai cair no catch
  }
}
