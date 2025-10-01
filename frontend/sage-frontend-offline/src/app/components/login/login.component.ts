import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { CommonModule, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { Router } from '@angular/router';
import { CaregiverLeaderService } from '../../controller/caregiver-leader/caregiver-leader.service';
import { RecoverPasswordComponent } from '../recover-password/recover-password.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    NgIf,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
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
    private dialog: MatDialog
  ) { }
  ngOnInit(): void {
    this.checkIfHasCaregiverLeader();
    this.loginIfCredentialsIsValid();
  }

  async checkIfHasCaregiverLeader() {
    const hasCaregiverLeader =
      await this.caregiverLeaderService.countAllActiveCaregiverLeader();
    console.log(hasCaregiverLeader);
    this.registerButtonEnabled = hasCaregiverLeader === 0;
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

  esqueciSenha() {
    this.dialog.open(RecoverPasswordComponent, {
      width: '400px',
      disableClose: false, 
    });
  }


}
