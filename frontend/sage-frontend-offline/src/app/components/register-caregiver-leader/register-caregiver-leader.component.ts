import { Component } from '@angular/core';
import { ButtonComponent } from '../button/button.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-caregiver-leader',
  standalone: true,
  imports: [ButtonComponent],
  templateUrl: './register-caregiver-leader.component.html',
  styleUrl: './register-caregiver-leader.component.scss'
})
export class RegisterCaregiverLeaderComponent {

  constructor(
    private router: Router,
  ) { }

  cadastrar() {
    this.router.navigate(['nurse-manager/register']);
  }

}
