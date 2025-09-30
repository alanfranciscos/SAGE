import { Component } from '@angular/core';
import { RegisterNurseComponentComponent } from '../../components/register-nurse-component/register-nurse-component.component';
import { ListNurseComponent } from '../../components/list-nurse-component/list-nurse-component.component';
import { InstructionsComponent } from '../../components/instructions/instructions.component';
import { MainComponent } from '../../layout/main/main.component';
import { RegisterCaregiverLeaderComponent } from "../../components/register-caregiver-leader/register-caregiver-leader.component";

@Component({
  selector: 'app-nurse-manager',
  standalone: true,
  imports: [
    RegisterNurseComponentComponent,
    ListNurseComponent,
    InstructionsComponent,
    MainComponent,
    RegisterCaregiverLeaderComponent
],
  templateUrl: './nurse-manager.component.html',
  styleUrl: './nurse-manager.component.scss',
})
export class NurseManagerComponent {}
