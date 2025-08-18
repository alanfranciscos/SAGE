import { Component } from '@angular/core';
import { MainComponent } from "../../layout/main/main.component";

@Component({
  selector: 'app-alerts',
  standalone: true,
  imports: [MainComponent],
  templateUrl: './alerts.component.html',
  styleUrl: './alerts.component.scss'
})
export class AlertsComponent {

}
