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
}
