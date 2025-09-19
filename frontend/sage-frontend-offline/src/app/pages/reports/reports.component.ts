import { Component } from '@angular/core';
import { AlertComponent } from '../../components/alert/alert.component';
import { MainComponent } from '../../layout/main/main.component';
import { SummaryCardComponent } from '../../components/summary-card/summary-card.component';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [AlertComponent, MainComponent, SummaryCardComponent],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.scss',
})
export class ReportsComponent {}
