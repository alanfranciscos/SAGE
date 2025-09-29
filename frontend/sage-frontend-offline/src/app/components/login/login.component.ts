import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { Router } from '@angular/router';

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
export class LoginComponent {
  email = '';
  password = '';

  constructor(
    private dialogRef: MatDialogRef<LoginComponent>,
    private authService: AuthenticationService,
    private router: Router,
  ) { }

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
    this.dialogRef.close(undefined); // ou apenas this.dialogRef.close();
  }
}