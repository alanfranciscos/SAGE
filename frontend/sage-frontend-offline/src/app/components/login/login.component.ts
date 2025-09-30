import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { Router } from '@angular/router';
import { CaregiverLeaderService } from '../../controller/caregiver-leader/caregiver-leader.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule,
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
  email = '';
  password = '';
  public registerButtonEnabled: boolean = false;

  constructor(
    private dialogRef: MatDialogRef<LoginComponent>,
    private authService: AuthenticationService,
    private router: Router,
    private caregiverLeaderService: CaregiverLeaderService,
  ) { }
  ngOnInit(): void {
    this.checkIfHasCaregiverLeader();
    this.loginIfCredentialsIsValid();
  }

  async checkIfHasCaregiverLeader(){
    const hasCaregiverLeader = await this.caregiverLeaderService
  }

  async login() {
    const success = await this.authService.login(this.email, this.password);
    if (success) {
      this.dialogRef.close(true);
    } else {
      alert('Email ou senha incorretos');
    }
  }

  cadastrar() {
    this.router.navigate(['nurse-manager/register']);
    this.dialogRef.close(undefined);
  }

    loginIfCredentialsIsValid() {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/']);
    }
  }
}