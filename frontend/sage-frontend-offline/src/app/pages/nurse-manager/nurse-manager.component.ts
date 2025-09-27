import { Component } from '@angular/core';
import { RegisterNurseComponentComponent } from '../../components/register-nurse-component/register-nurse-component.component';
import { ListNurseComponent } from '../../components/list-nurse-component/list-nurse-component.component';
import { InstructionsComponent } from '../../components/instructions/instructions.component';
import { MainComponent } from '../../layout/main/main.component';

@Component({
  selector: 'app-nurse-manager',
  standalone: true,
  imports: [
    RegisterNurseComponentComponent,
    ListNurseComponent,
    InstructionsComponent,
    MainComponent,
  ],
  templateUrl: './nurse-manager.component.html',
  styleUrl: './nurse-manager.component.scss',
})
export class NurseManagerComponent {}
