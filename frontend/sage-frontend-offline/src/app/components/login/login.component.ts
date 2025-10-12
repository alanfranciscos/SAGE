import { Component, OnInit, OnDestroy } from '@angular/core';
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
export class LoginComponent implements OnInit, OnDestroy {
  email = '';
  password = '';
  public registerButtonEnabled: boolean = false;
  public loginError: string = '';
  private loginAttempts: number = 0;
  public blockUntil: number | null = null;

  public currentTime: number = Date.now();
  private timerInterval: any;

  constructor(
    private dialogRef: MatDialogRef<LoginComponent>,
    private authService: AuthenticationService,
    private router: Router,
    private caregiverLeaderService: CaregiverLeaderService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.checkIfHasCaregiverLeader();
    this.loginIfCredentialsIsValid();

    const storedBlock = localStorage.getItem('blockUntil');
    if (storedBlock) {
      this.blockUntil = Number(storedBlock);
    }

    this.updateLoginError();

    this.timerInterval = setInterval(() => {
      this.currentTime = Date.now();
      this.updateLoginError();
    }, 1000);
  }

  ngOnDestroy(): void {
    if (this.timerInterval) clearInterval(this.timerInterval);
  }

  async checkIfHasCaregiverLeader() {
    const hasCaregiverLeader = await this.caregiverLeaderService.countAllActiveCaregiverLeader();
    this.registerButtonEnabled = hasCaregiverLeader === 0;
  }

  async login() {
    if (this.blockUntil && this.currentTime < this.blockUntil) {
      this.updateLoginError();
      return;
    }

    const success = await this.authService.login(this.email, this.password);
    if (success) {
      this.resetBlock();
      this.dialogRef.close(true);
    } else {
      this.loginAttempts++;
      if (this.loginAttempts >= 3) {
        this.blockUntil = this.currentTime + 30 * 60 * 1000;
        localStorage.setItem('blockUntil', this.blockUntil.toString());
      }
      this.updateLoginError();
    }
  }

  private updateLoginError() {
    if (this.blockUntil && this.currentTime < this.blockUntil) {
      const totalSeconds = Math.floor((this.blockUntil - this.currentTime) / 1000);
      const minutes = Math.floor(totalSeconds / 60);
      const seconds = totalSeconds % 60;
      const formattedSeconds = seconds.toString().padStart(2, '0');

      this.loginError = `Você excedeu o número de tentativas. Tente novamente em ${minutes}m ${formattedSeconds}s.`;
    } else {
      this.blockUntil = null;
      localStorage.removeItem('blockUntil');

      if (this.loginAttempts > 0) {
        this.loginError = 'Credenciais de acesso incorretas. Verifique seu email e senha.';
      } else {
        this.loginError = '';
      }
    }
  }

  private resetBlock() {
    this.blockUntil = null;
    this.loginAttempts = 0;
    this.loginError = '';
    localStorage.removeItem('blockUntil');
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
    this.dialogRef.close();
    this.dialog.open(RecoverPasswordComponent, {
      width: '400px',
      disableClose: false,
    });
  }
}