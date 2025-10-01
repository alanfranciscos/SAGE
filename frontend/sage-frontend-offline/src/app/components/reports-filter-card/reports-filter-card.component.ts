import { Component, EventEmitter, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-reports-filter-card',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatDatepickerModule,
    MatSelectModule,
  ],
  template: `
    <mat-card>
      <mat-card-header>
        <mat-card-title>Filtros de Relatórios</mat-card-title>
      </mat-card-header>

      <mat-card-content>
        <div class="filters-grid">
          <!-- Período -->
          <mat-form-field appearance="outline">
            <mat-label>Período</mat-label>
            <mat-date-range-input [rangePicker]="picker">
              <input
                matStartDate
                placeholder="Início"
                [formControl]="dateRangeStart"
              />
              <input
                matEndDate
                placeholder="Fim"
                [formControl]="dateRangeEnd"
              />
            </mat-date-range-input>
            <mat-datepicker-toggle
              matSuffix
              [for]="picker"
            ></mat-datepicker-toggle>
            <mat-date-range-picker #picker></mat-date-range-picker>
          </mat-form-field>

          <!-- Residente -->
          <mat-form-field appearance="outline">
            <mat-label>Residente</mat-label>
            <mat-icon matPrefix>search</mat-icon>
            <input matInput [formControl]="residentFilter" />
          </mat-form-field>

          <!-- Equipe -->
          <mat-form-field appearance="outline">
            <mat-label>Equipe/Profissional</mat-label>
            <mat-icon matPrefix>search</mat-icon>
            <input matInput [formControl]="teamFilter" />
          </mat-form-field>

          <!-- Tipo de Alarme -->
          <mat-form-field appearance="outline">
            <mat-label>Tipo de Alarme</mat-label>
            <mat-select [formControl]="alarmType">
              <mat-option
                *ngFor="let option of alarmTypes"
                [value]="option.value"
              >
                {{ option.label }}
              </mat-option>
            </mat-select>
          </mat-form-field>
        </div>
      </mat-card-content>
    </mat-card>
  `,
  styles: [
    `
      .filters-grid {
        display: grid;
        gap: 16px;
        grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
      }
    `,
  ],
})
export class ReportsFilterCardComponent {
  // Valores padrão para teste
  dateRangeStart = new FormControl(new Date());
  dateRangeEnd = new FormControl(new Date());
  residentFilter = new FormControl('Leandro');
  teamFilter = new FormControl('Equipe A');
  alarmType = new FormControl('all');

  alarmTypes = [
    { value: 'all', label: 'Todos' },
    { value: 'queda', label: 'Queda' },
    { value: 'medicacao', label: 'Medicação' },
    { value: 'auxilio', label: 'Auxílio' },
    { value: 'falso-alarme', label: 'Falso Alarme' },
    { value: 'emergencia', label: 'Emergência' },
  ];

  @Output() filtersChange = new EventEmitter<{
    start: Date;
    end: Date;
    resident: string;
    team: string;
    alarmType: string;
  }>();

  emitFilters() {
    this.filtersChange.emit({
      start: this.dateRangeStart.value ?? new Date(),
      end: this.dateRangeEnd.value ?? new Date(),
      resident: this.residentFilter.value ?? '',
      team: this.teamFilter.value ?? '',
      alarmType: this.alarmType.value ?? 'all',
    });
  }
}
