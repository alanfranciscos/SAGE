import {
  Component,
  EventEmitter,
  inject,
  Input,
  Output,
  OnInit,
  OnDestroy,
} from '@angular/core';
import { NgClass } from '@angular/common';
import { Router } from '@angular/router';
import { AccessibilityService } from '../../controller/accessibility/accessibility.service';
import { Subscription } from 'rxjs';
import { ButtonComponent } from '../button/button.component';

@Component({
  selector: 'app-dashboard-resident-card',
  standalone: true,
  imports: [NgClass, ButtonComponent],
  templateUrl: './dashboard-resident-card.component.html',
  styleUrl: './dashboard-resident-card.component.scss',
})
export class DashboardResidentCardComponent implements OnInit, OnDestroy {
  @Input() residentId!: string;
  @Input() residentImage: string = '';
  @Input() residentName: string = '';
  @Input() houseNumber: string = '';
  @Input() lastCallDateTime: string = '';
  @Input() status: 'normal' | 'warning' | 'critical' = 'normal';
  @Input() totalCallsLast24Hours: string = '';

  @Output() openDetails = new EventEmitter<string>();

  daltonicMode = false;
  private accessibilitySub?: Subscription;

  private router = inject(Router);
  private accessibilityService = inject(AccessibilityService);

  statusMap = {
    normal: {
      text: 'Normal',
      icon: 'fa-solid fa-circle-check',
      color: '#0069C5',
    },
    warning: {
      text: 'Alerta',
      icon: 'fa-solid fa-triangle-exclamation',
      color: 'orange',
    },
    critical: {
      text: 'Crítico',
      icon: 'fa-solid fa-triangle-exclamation',
      color: 'red',
    },
  };

  ngOnInit() {
    // Inscreve-se no serviço para receber updates do modo daltônico
    this.accessibilitySub = this.accessibilityService.daltonicMode$.subscribe(
      (enabled) => (this.daltonicMode = enabled)
    );
  }

  ngOnDestroy() {
    this.accessibilitySub?.unsubscribe();
  }

  onCardClick() {
    this.openDetails.emit(this.residentId);
  }

  onButtonClick() {
    if (this.status === 'normal') {
      this.openDetails.emit(this.residentId);
    } else {
      this.router.navigate(['/alerts']);
    }
  }

  getStatusText(): string {
    return this.statusMap[this.status]?.text || 'Desconhecido';
  }

  getStatusIcon(): string {
    return this.statusMap[this.status]?.icon || 'fa-solid fa-circle';
  }

  getStatusColor(): string {
    if (this.daltonicMode) {
      // cores adaptadas para daltônicos
      switch (this.status) {
        case 'warning':
          return '#FFD700'; // amarelo mais claro
        case 'critical':
          return '#FF6B6B'; // vermelho mais suave
        case 'normal':
          return '#4DA6FF'; // azul mais intenso
      }
    }
    return this.statusMap[this.status]?.color || '#ccc';
  }

  get formattedLastCall(): string {
    if (!this.lastCallDateTime) return '---';
    const isoString = this.lastCallDateTime
      .replace(' ', 'T')
      .replace('+00', 'Z');
    const date = new Date(isoString);
    if (isNaN(date.getTime())) return '---';
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${day}/${month}/${year} ${hours}:${minutes}`;
  }
}
