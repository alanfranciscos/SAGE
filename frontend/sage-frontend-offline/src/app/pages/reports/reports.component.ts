import { Component } from '@angular/core';
import { AlertComponent } from "../../components/alert/alert.component";
import { MainComponent } from "../../layout/main/main.component";

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [AlertComponent, MainComponent],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.scss'
})
export class ReportsComponent {

}
