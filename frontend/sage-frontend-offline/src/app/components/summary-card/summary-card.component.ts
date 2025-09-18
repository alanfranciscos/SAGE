import { Component } from '@angular/core';

@Component({
  selector: 'app-summary-card',
  standalone: true,
  imports: [],
  templateUrl: './summary-card.component.html',
  styleUrl: './summary-card.component.scss'
})
export class SummaryCardComponent {

  title: string = 'Summary Card Title';
  value: string = '8';
  icon: string = 'icon-class';

}
