import { Component } from '@angular/core';
import { RegisterNurseComponentComponent } from '../../components/register-nurse-component/register-nurse-component.component';

@Component({
  selector: 'app-generico',
  standalone: true,
  imports: [RegisterNurseComponentComponent],
  templateUrl: './generico.component.html',
  styleUrl: './generico.component.scss',
})
export class GenericoComponent {}
