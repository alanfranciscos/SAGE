import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-header-card-report',
  templateUrl: './header-card-report.component.html',
  styleUrls: ['./header-card-report.component.scss'],
  standalone: true, // se você estiver usando standalone components
  imports: [CommonModule],
})
export class HeaderCardReportComponent {
  @Input() title!: string;
  @Input() value!: string | number;
  @Input() delta?: string;

  @Input() icon?: string; // ex: 'notifications', 'timer', 'schedule'
  @Input() iconBgColor?: string; // ex: 'bg-blue-100'
  @Input() iconColor?: string; // ex: 'text-blue-600'

  @Input() valueClass: string = 'value-default';
}
