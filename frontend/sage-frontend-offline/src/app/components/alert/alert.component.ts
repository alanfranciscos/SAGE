import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

type AlertType = 'warning' | 'several';

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.scss',
})
export class AlertComponent {
  @Input() message: string = 'Existem alertas ativos';
  @Input() type!: AlertType;
  alertStyle!: string;

  ngOnInit() {
    this.setAlertStyle();
  }

  private setAlertStyle() {
    switch (this.type) {
      case 'warning':
        this.alertStyle = 'alert-warning';
        break;
      case 'several':
        this.alertStyle = 'alert-several';
        break;
    }
  }
}
