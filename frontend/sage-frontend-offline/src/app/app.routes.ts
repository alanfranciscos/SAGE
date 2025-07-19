import { Routes } from '@angular/router';
import { ListResidentsComponent } from './pages/list-residents/list-residents.component';
import { RegisterComponent } from './layout/register/register.component';
import { RegisterResidentComponent } from './pages/register-resident/register-resident.component';

export const routes: Routes = [
  {
    path: '',
    component: ListResidentsComponent,
  },
  {
    path: 'residents',
    component: ListResidentsComponent,
  },
  {
    path: 'residents/register',
    component: RegisterResidentComponent,
  },
];
