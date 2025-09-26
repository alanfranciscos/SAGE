import { Injectable } from '@angular/core';
import { ApiService } from '../../services/api/api.service';
import { Control } from '../../model/Control';

@Injectable({
  providedIn: 'root',
})
export class ControlService {
  api: ReturnType<ApiService['getApi']>;

  constructor(private apiService: ApiService) {
    this.api = this.apiService.getApi();
  }

  /** Retorna todos os controles (ocupados e livres) */
  async getControls(): Promise<Control[]> {
    const response = await this.api.get<{ listControl: Control[] }>(
      'http://localhost:8080/api/v1/control'
    );

    if (response.status !== 200) {
      throw new Error('Failed to fetch controls');
    }

    return response.data.listControl;
  }

  /** Retorna apenas os controles que NÃO têm residentId (disponíveis para associar) */
  /** Retorna apenas os números de controle disponíveis (de 1 até 100) */
  async getAvailableControls(): Promise<number[]> {
    const allControls = await this.getControls(); // já cadastrados
    const usedIds = allControls.map((c) => c.controlId);

    // gera lista de 1 a 100
    const allIds = Array.from({ length: 100 }, (_, i) => i + 1);

    // remove os já usados
    return allIds.filter((id) => !usedIds.includes(id));
  }

  /** Verifica se um controlId está disponível (sem residentId associado) */
  async isControlAvailable(controlId: number): Promise<boolean> {
    const allControls = await this.getControls();
    const control = allControls.find((c) => c.controlId === controlId);
    return !!control && !control.residentId;
  }

  /** Associa um controle a um residente */
  async assignControl(controlId: number, residentId: string): Promise<void> {
    const response = await this.api.put(
      `http://localhost:8080/api/v1/control/${controlId}/assign`,
      { residentId }
    );

    if (response.status !== 200) {
      throw new Error('Failed to assign control');
    }
  }

  /** Remove associação de um controle (deixa ele disponível novamente) */
  async unassignControl(controlId: number): Promise<void> {
    const response = await this.api.put(
      `http://localhost:8080/api/v1/control/${controlId}/unassign`,
      {}
    );

    if (response.status !== 200) {
      throw new Error('Failed to unassign control');
    }
  }
}
