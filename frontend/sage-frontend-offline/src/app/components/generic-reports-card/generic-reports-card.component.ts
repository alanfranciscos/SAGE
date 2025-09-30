import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-generic-reports-card',
  standalone: true,
  imports: [MatCardModule, NgFor],
  templateUrl: './generic-reports-card.component.html',
  styleUrls: ['./generic-reports-card.component.scss'],
})
export class GenericReportsCardComponent {
  @Input() title: string = 'Motivos de Chamado';

  // Dados de exemplo – podem ser passados via @Input()
  reports = [
    { label: 'Auxílio Geral', value: 65, color: 'blue' },
    { label: 'Medicação', value: 45, color: 'emerald' },
    { label: 'Queda', value: 25, color: 'red' },
    { label: 'Emergência', value: 15, color: 'orange' },
    { label: 'Falso Alarme', value: 8, color: 'gray' },
  ];
}
