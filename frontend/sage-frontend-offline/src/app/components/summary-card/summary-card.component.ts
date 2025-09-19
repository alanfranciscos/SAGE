import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-summary-card',
  standalone: true,
  imports: [NgClass],
  templateUrl: './summary-card.component.html',
  styleUrl: './summary-card.component.scss',
})
export class SummaryCardComponent {
  @Input() title!: string;
  @Input() value!: string | number;
  // @Input() icon!: string; // ex: 'user', 'chart-line', 'gear'
  @Input() icon!: 'residents' | 'calls' | 'resolved' | 'time';

  @Input() color: 'red' | 'green' | 'default' = 'default';

  private iconMap: Record<string, string> = {
    residents: 'fa-solid fa-user',
    calls: 'fa-solid fa-bell',
    resolved: 'fa-solid fa-square-check',
    time: 'fa-solid fa-clock',
  };
  get iconClass(): string {
    return this.iconMap[this.icon] || 'fa-solid fa-circle-question';
    // fallback caso não encontre
  }
  get valueClass(): string {
    return `value-${this.color}`;
  }
}
