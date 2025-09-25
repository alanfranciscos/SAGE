import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonComponent } from '../button/button.component';

interface RegisterNurse {
  name: string;
  cpf: string;
  email: string;
  tel: string;
  token: string;
}

@Component({
  selector: 'app-register-nurse-component',
  standalone: true,
  imports: [FormsModule, ButtonComponent],
  templateUrl: './register-nurse-component.component.html',
  styleUrl: './register-nurse-component.component.scss',
})
export class RegisterNurseComponentComponent {
  @Input() nome: string = '';
  @Input() cpf: string = '';
  @Input() email: string = '';
  @Input() tel: string = '';
  @Input() token: string = '';

  nurse = {
    name: 'Maria',
    cpf: '12312312',
    email: 'asdsaa@asdasd.com',
    tel: '123231323',
    token: '123123',
  };
  onRegisterNurse() {
    throw new Error('Method not implemented.');
  }
}
