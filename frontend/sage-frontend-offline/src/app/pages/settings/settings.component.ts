import { Component } from '@angular/core';
import { MainComponent } from "../../layout/main/main.component";
import { ResidentService } from '../../controller/resident/resident.service';
import { Router } from '@angular/router';
import { PanelSettingsComponent } from '../../components/panel-settings/panel-settings.component';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    MainComponent,
    PanelSettingsComponent,
    ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss'
})
export class SettingsComponent {
  constructor(
    private residentService: ResidentService,
    private router: Router
  ) {}
  async ngOnInit(): Promise<void> {
    try {
    } catch (error) {}
  }

}
