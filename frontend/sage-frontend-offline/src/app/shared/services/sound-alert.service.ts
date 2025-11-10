import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class SoundAlertService {
  private audio: HTMLAudioElement | null = null;
  private isPlaying = false;
  private hasCriticalAlert = false;

  constructor() {
    this.audio = new Audio('assets/sounds/emergency.mp3');
    this.audio.loop = true;
    this.audio.volume = 0.9;
  }

  /** Atualiza o estado global de alerta crítico */
  updateCriticalState(hasCritical: boolean): void {
    this.hasCriticalAlert = hasCritical;
    if (hasCritical) {
      this.play();
    } else {
      this.stop();
    }
  }

  private play(): void {
    if (!this.isPlaying && this.audio) {
      this.audio.play().catch(err => console.error('Erro ao tocar som:', err));
      this.isPlaying = true;
    }
  }

  private stop(): void {
    if (this.audio && this.isPlaying) {
      this.audio.pause();
      this.audio.currentTime = 0;
      this.isPlaying = false;
    }
  }

  /** Permite que outras telas saibam o estado atual */
  isCriticalActive(): boolean {
    return this.hasCriticalAlert;
  }
}
