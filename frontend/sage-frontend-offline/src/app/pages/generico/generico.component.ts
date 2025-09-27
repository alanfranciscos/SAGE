import { Component } from '@angular/core';
import { RegisterNurseComponentComponent } from '../../components/register-nurse-component/register-nurse-component.component';
import { ListNurseComponent } from '../../components/list-nurse-component/list-nurse-component.component';
import { EditNurseDialogComponent } from '../../components/update-nurse-modal/update-nurse-modal.component';
import { InstructionsComponent } from '../../components/instructions/instructions.component';

@Component({
  selector: 'app-generico',
  standalone: true,
  imports: [
    RegisterNurseComponentComponent,
    ListNurseComponent,
    EditNurseDialogComponent,
    InstructionsComponent,
  ],
  templateUrl: './generico.component.html',
  styleUrl: './generico.component.scss',
})
export class GenericoComponent {}
