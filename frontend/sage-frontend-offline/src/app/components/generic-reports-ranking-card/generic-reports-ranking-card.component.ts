import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-generic-reports-ranking-card',
  standalone: true,
  imports: [MatCardModule, NgFor],
  templateUrl: './generic-reports-ranking-card.component.html',
  styleUrls: ['./generic-reports-ranking-card.component.scss'],
})
export class GenericReportsRankingCardComponent {
  @Input() title: string = 'Top 5 Residentes com Mais Chamados';

  // Lista de residentes (mock default)
  residents = [
    { name: 'Ana Costa', room: 'Quarto 150', calls: 18, avgResponse: '3m 12s' },
    {
      name: 'Maria Silva',
      room: 'Quarto 101',
      calls: 15,
      avgResponse: '2m 45s',
    },
    {
      name: 'João Santos',
      room: 'Quarto 205',
      calls: 12,
      avgResponse: '4m 18s',
    },
    {
      name: 'Carlos Oliveira',
      room: 'Quarto 302',
      calls: 11,
      avgResponse: '3m 55s',
    },
    {
      name: 'Helena Ferreira',
      room: 'Quarto 108',
      calls: 9,
      avgResponse: '2m 38s',
    },
  ];
}
