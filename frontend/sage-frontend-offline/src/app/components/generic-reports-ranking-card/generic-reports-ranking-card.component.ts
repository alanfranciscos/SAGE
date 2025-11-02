import { Component, Input, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { CommonModule, NgFor } from '@angular/common';

export interface ResidentPerformance {
  name: string;
  room?: string; // se quiser mostrar quarto, opcional
  calls: number;
  avgResponse: string | null;
}

@Component({
  selector: 'app-generic-reports-ranking-card',
  standalone: true,
  imports: [MatCardModule, NgFor, CommonModule],
  templateUrl: './generic-reports-ranking-card.component.html',
  styleUrls: ['./generic-reports-ranking-card.component.scss'],
})
export class GenericReportsRankingCardComponent {
  @Input() title: string = '5 Residentes com Mais Chamados';
  @Input() residents: ResidentPerformance[] = [];
}
