import { CommonModule, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AuthenticationService } from '../../services/authentication/authentication.service';

@Component({
  selector: 'app-recover-password',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NgIf,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './recover-password.component.html',
  styleUrl: './recover-password.component.scss'
})
export class RecoverPasswordComponent {
  email = '';
  token = '';
  novaSenha = '';
  confirmarSenha = '';
  tokenSent = false;

  constructor(private authService: AuthenticationService) { }

  async enviarSolicitacao() {
    try {
      await this.authService.sendRecoveryToken(this.email);
      this.tokenSent = true;
      alert('Código enviado para seu email.');
    } catch (error) {
      alert('Falha ao enviar código. Verifique o email.');
    }
  }

  async redefinirSenha() {
    if (this.novaSenha !== this.confirmarSenha) {
      alert('As senhas não coincidem!');
      return;
    }

    try {
      await this.authService.resetPassword(this.email, this.token, this.novaSenha, this.confirmarSenha);
      alert('Senha redefinida com sucesso!');
    } catch (error) {
      alert('Falha ao redefinir senha. Verifique o token e tente novamente.');
    }
  }
}
