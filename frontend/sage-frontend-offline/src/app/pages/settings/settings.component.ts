import { Component } from '@angular/core';
import { MainComponent } from "../../layout/main/main.component";

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [MainComponent],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss'
})
export class SettingsComponent {

}
