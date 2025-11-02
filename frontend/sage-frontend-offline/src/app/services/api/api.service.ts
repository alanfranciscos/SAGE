import { Injectable } from '@angular/core';
import axios, { AxiosInstance } from 'axios';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  // baseURL ajustado para funcionar tanto local quanto empacotado
  baseURL = window.location.origin; // ← chave da correção!

  api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: this.baseURL,
    });
  }

  getApi = (): AxiosInstance => {
    return this.api;
  };

  setToken = (token: string) => {
    this.api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  };
}
