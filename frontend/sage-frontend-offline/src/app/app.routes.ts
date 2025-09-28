import { Routes } from '@angular/router';
// import { ListResidentsComponent } from './pages/list-residents/list-residents.component';
import { RegisterComponent } from './layout/register/register.component';
import { RegisterResidentComponent } from './pages/register-resident/register-resident.component';
import { AlertsComponent } from './pages/alerts/alerts.component';
import { ReportsComponent } from './pages/reports/reports.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { GenericoComponent } from './pages/generico/generico.component';
import { NurseManagerComponent } from './pages/nurse-manager/nurse-manager.component';
import { UpdateResidentComponent } from './pages/update-resident/update-resident.component';

export const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
  },
  {
    path: 'residents/register',
    component: RegisterResidentComponent,
  },
  {
    path: 'residents/update/:id',
    component: UpdateResidentComponent,
  },
  {
    path: 'alerts',
    component: AlertsComponent,
  },
  {
    path: 'reports',
    component: ReportsComponent,
  },
  {
    path: 'settings',
    component: SettingsComponent,
  },
  {
    path: 'nurse-manager',
    component: NurseManagerComponent,
  },
  {
    path: '**',
    component: DashboardComponent,
  },
];