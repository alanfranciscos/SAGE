import { Component } from '@angular/core';
import { MainComponent } from "../../layout/main/main.component";
import { SearchInputComponent } from "../../components/search-input/search-input.component";

@Component({
  selector: 'app-alerts',
  standalone: true,
  imports: [MainComponent, SearchInputComponent],
  templateUrl: './alerts.component.html',
  styleUrl: './alerts.component.scss'
})
export class AlertsComponent {

  constructor(){}

  async onSearch(searchTerm: string) {
    console.log('Search term:', searchTerm);
  }

}
