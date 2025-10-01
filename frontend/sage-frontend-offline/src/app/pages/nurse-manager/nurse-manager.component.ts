import { Component } from '@angular/core';
import { RegisterNurseComponentComponent } from '../../components/register-nurse-component/register-nurse-component.component';
import { ListNurseComponent } from '../../components/list-nurse-component/list-nurse-component.component';
import { InstructionsComponent } from '../../components/instructions/instructions.component';
import { MainComponent } from '../../layout/main/main.component';
import { RegisterCaregiverLeaderComponent } from "../../components/register-caregiver-leader/register-caregiver-leader.component";
import { NgIf } from '@angular/common';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nurse-manager',
  standalone: true,
  imports: [
    NgIf,
    RegisterNurseComponentComponent,
    ListNurseComponent,
    InstructionsComponent,
    MainComponent,
    RegisterCaregiverLeaderComponent
  ],
  templateUrl: './nurse-manager.component.html',
  styleUrl: './nurse-manager.component.scss',
})
export class NurseManagerComponent {
  isUserMenuOpen = false;
  userName: string | null = null;

  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {
    this.userName = this.authService.getUserName();
  }

  toggleUserMenu() {
    this.isUserMenuOpen = !this.isUserMenuOpen;
  }

  closeUserMenu() {
    setTimeout(() => {
      this.isUserMenuOpen = false;
    }, 150);
  }

  onEditProfile(event: Event) {
    event.stopPropagation();
    console.log('Editar perfil clicado');
  }

  onLogout(event: Event) {
    event.stopPropagation();
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

