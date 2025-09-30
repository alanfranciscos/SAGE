import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-reports-filter-card',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCardModule,
    MatIconModule,
  ],
  templateUrl: './reports-filter-card.component.html',
  styleUrls: ['./reports-filter-card.component.scss'],
})
export class ReportsFilterCardComponent {
  @Input() dateRange!: { start: Date; end: Date };
  @Output() dateRangeChange = new EventEmitter<{ start: Date; end: Date }>();

  residentFilter = new FormControl('');
  teamFilter = new FormControl('');
  alarmType = new FormControl('all');

  alarmTypes = [
    { value: 'all', label: 'Todos' },
    { value: 'queda', label: 'Queda' },
    { value: 'medicacao', label: 'Medicação' },
    { value: 'auxilio', label: 'Auxílio' },
    { value: 'falso-alarme', label: 'Falso Alarme' },
    { value: 'emergencia', label: 'Emergência' },
  ];
}
