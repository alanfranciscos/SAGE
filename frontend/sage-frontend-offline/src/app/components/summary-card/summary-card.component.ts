import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-summary-card',
  standalone: true,
  imports: [NgClass],
  templateUrl: './summary-card.component.html',
  styleUrl: './summary-card.component.scss'
})
export class SummaryCardComponent {

 @Input() title!: string;
  @Input() value!: string | number;
  // @Input() icon!: string; // ex: 'user', 'chart-line', 'gear'
  @Input() icon!: 'user' | 'event' | 'github' | 'settings';

   @Input() color: 'red' | 'green' | 'default'  = 'default';

 private iconMap: Record<string, string> = {
    user: 'fa-solid fa-user',
    event: 'fa-solid fa-calendar-days',
    github: 'fa-brands fa-github',
    settings: 'fa-solid fa-gear'
  };
    get iconClass(): string {
    return this.iconMap[this.icon] || 'fa-solid fa-circle-question'; 
    // fallback caso não encontre
  }
    get valueClass(): string {
    return `value-${this.color}`;
  }
}