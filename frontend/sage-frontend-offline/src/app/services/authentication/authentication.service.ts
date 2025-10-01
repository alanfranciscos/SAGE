import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private loggedIn = false;
  private token: string | null = null;
  private userName: string | null = null;

  constructor(private apiService: ApiService) {
    this.restoreSession();
  }

  private restoreSession(): void {
    const storedToken = localStorage.getItem('token');
    const storedName = localStorage.getItem('name');

    if (storedToken) {
      this.token = storedToken;
      this.loggedIn = true;
      this.apiService.setToken(storedToken);
      this.userName = storedName;
    } else {
      this.loggedIn = false;
    }
  }

  isLoggedIn(): boolean {
    return this.loggedIn;
  }

  getToken(): string | null {
    return this.token;
  }

  getUserName(): string | null {
    return this.userName;
  }

  async login(email: string, password: string): Promise<boolean> {
    try {
      const response = await this.apiService.getApi().post<any>('/api/auth/login', {
        email,
        password
      });

      const data = response.data;
      this.token = data.token;
      this.userName = data.name;
      this.loggedIn = true;

      if (this.token) {
        localStorage.setItem('token', this.token);
      }

      if (this.userName) {
        localStorage.setItem('name', this.userName);
      }

      this.apiService.getApi().defaults.headers.common['Authorization'] = `Bearer ${this.token}`;
      return true;
    } catch (error) {
      console.error('Falha no login', error);
      this.loggedIn = false;
      return false;
    }
  }

  isAuthenticated(): boolean {
    return localStorage.getItem('token') !== null;
  }

  logout(): void {
    this.loggedIn = false;
    this.token = null;
    this.userName = null;
    localStorage.removeItem('token');
    localStorage.removeItem('name');
    this.apiService.getApi().defaults.headers.common['Authorization'] = undefined;
  }
}
