import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { NgClass } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-resident-card',
  standalone: true,
  imports: [NgClass],
  templateUrl: './dashboard-resident-card.component.html',
  styleUrl: './dashboard-resident-card.component.scss',
})
export class DashboardResidentCardComponent {
  @Input() residentId!: string; // <-- novo
  @Input() residentImage: string = '';
  @Input() residentName: string = '';
  @Input() houseNumber: string = '';
  @Input() lastCallDateTime: string = '';
  @Input() status: 'normal' | 'warning' | 'critical' = 'normal';
  @Input() lastCallTime: string = ''; // tira isso ###########################################
  @Input() totalCallsLast24Hours: string = '';

  @Output() openDetails = new EventEmitter<string>(); // <-- evento para o pai
  default_user_avatar: any;
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
  get color(): string {
    switch (this.status) {
      case 'warning':
        return 'orange';
      case 'critical':
        return 'red';
      case 'normal':
      default:
        return 'white';
    }
  }

  onCardClick() {
    this.openDetails.emit(this.residentId); // avisa o pai passando o id
  }
  private router = inject(Router);
  onButtonClick() {
    if (this.status === 'normal') {
      this.openDetails.emit(this.residentId); // abre modal
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
    return this.statusMap[this.status]?.color || '#ccc';
  }
  get formattedLastCall(): string {
    if (!this.lastCallDateTime) return '---';

    // garante formato ISO: "2025-10-01 09:30:00+00" -> "2025-10-01T09:30:00Z"
    const isoString = this.lastCallDateTime
      .replace(' ', 'T')
      .replace('+00', 'Z');

    const date = new Date(isoString);
    if (isNaN(date.getTime())) return '---';

    // usa horário local
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${day}/${month}/${year} ${hours}:${minutes}`;
  }
}
