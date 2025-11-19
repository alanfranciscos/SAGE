import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AccessibilityService {
  private daltonicModeSubject: BehaviorSubject<boolean>;

  daltonicMode$;

  constructor() {
    // Lê do localStorage ao iniciar
    const saved = localStorage.getItem('daltonicMode');
    const initial = saved === 'true'; // converte string para boolean
    this.daltonicModeSubject = new BehaviorSubject<boolean>(initial);
    this.daltonicMode$ = this.daltonicModeSubject.asObservable();
  }

  setDaltonicMode(enabled: boolean) {
    this.daltonicModeSubject.next(enabled);
    localStorage.setItem('daltonicMode', String(enabled));
  }

  toggleDaltonicMode() {
    const newValue = !this.daltonicModeSubject.value;
    this.setDaltonicMode(newValue);
  }

  isDaltonicModeEnabled(): boolean {
    return this.daltonicModeSubject.value;
  }
}
