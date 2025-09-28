import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AuthenticationService } from '../services/authentication/authentication.service';
import { LoginComponent } from '../components/login/login.component';

export const caregiverGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthenticationService);
  const dialog = inject(MatDialog);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }

  const ref = dialog.open(LoginComponent, {
    width: '400px',
    disableClose: true
  });

  ref.afterClosed().subscribe(result => {
    if (result === true) {
      router.navigateByUrl(state.url);
    }
  });

  return false;
};