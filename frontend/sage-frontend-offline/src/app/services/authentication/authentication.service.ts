import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private loggedIn = false;
  private token: string | null = null;

  constructor(private apiService: ApiService) { this.restoreSession(); }

  private restoreSession(): void {
        const storedToken = localStorage.getItem('token');
        if (storedToken) {
            this.token = storedToken;
            this.loggedIn = true;
            this.apiService.setToken(storedToken); 
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

  async login(email: string, password: string): Promise<boolean> {
    try {
      const response = await this.apiService.getApi().post<any>('/api/auth/login', {
        email: email,
        password: password
      });
      const data = response.data;
      this.token = data.token;
      this.loggedIn = true;

      if (this.token) {
        localStorage.setItem('token', this.token);
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
    let token = localStorage.getItem('token');

    if (token != null) {
      return true;
    }
    return false;
  }

  logout(): void {
    this.loggedIn = false;
    this.token = null;
    localStorage.removeItem('token');
    this.apiService.getApi().defaults.headers.common['Authorization'] = undefined;
  }
}
