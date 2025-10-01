import { CommonModule, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

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

  constructor(
    private authService: AuthenticationService,
    private toastr: ToastrService,
    private router: Router,
    public dialogRef: MatDialogRef<RecoverPasswordComponent>
  ) { }

  async enviarSolicitacao() {
    try {
      await this.authService.sendRecoveryToken(this.email);
      this.tokenSent = true;
      this.toastr.success('Código enviado para seu email.', 'Sucesso');
    } catch (error) {
      this.toastr.error('Verifique o email.', 'Falha ao enviar código');
    }
  }

  async redefinirSenha() {
    if (this.novaSenha !== this.confirmarSenha) {
      this.toastr.error('As senhas digitadas não coincidem!', 'Erro de Senha');
      return;
    }

    try {
      await this.authService.resetPassword(this.email, this.token, this.novaSenha, this.confirmarSenha);
      this.toastr.success('Sua senha foi redefinida com sucesso!', 'Sucesso');
      this.dialogRef.close();
      this.router.navigate(['/']);
    } catch (error) {
      this.toastr.error('Verifique o token e tente novamente.', 'Falha ao redefinir senha');
    }
  }
}
