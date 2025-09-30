import { Injectable } from '@angular/core';
import axios, { AxiosInstance } from 'axios';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  baseURL = 'http://localhost:8080';
  // token = localStorage.getItem('token');

  api = axios.create({
    baseURL: this.baseURL,
  });

  constructor() {
    // if (this.token) {
    //   this.api.defaults.headers.common[
    //     'Authorization'
    //   ] = `Bearer ${this.token}`;
    // }
  }

  getApi = (): AxiosInstance => {
    return this.api;
  };

  setToken = (token: string) => {
    this.api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  };

  // removeToken = () => {
  //   delete this.api.defaults.headers.common['Authorization'];
  // };
}
