import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-date-filter',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="date-filter">
      <label>
        Data Inicial:
        <input type="date" [(ngModel)]="startDate" />
      </label>
      <label>
        Data Final:
        <input type="date" [(ngModel)]="endDate" />
      </label>
      <button (click)="applyFilter()">Filtrar</button>
      <button (click)="clearFilter()">Limpar</button>
    </div>
  `,
  styles: [
    `
      .date-filter {
        display: flex;
        align-items: center;
        gap: 1rem;
        margin-bottom: 1.5rem;
        flex-wrap: wrap;
      }

      .date-filter label {
        display: flex;
        flex-direction: column;
        font-size: 0.9rem;
        color: #333;
      }

      .date-filter input[type='date'] {
        padding: 0.4rem 0.6rem;
        font-size: 1rem;
        border: 1px solid #ccc;
        border-radius: 6px;
        background-color: #fff;
        transition: border-color 0.2s, box-shadow 0.2s;
        cursor: pointer;
        width: 160px;
      }

      .date-filter input[type='date']:focus {
        border-color: #0078d4;
        box-shadow: 0 0 0 2px rgba(0, 120, 212, 0.2);
        outline: none;
      }

      .date-filter button {
        padding: 0.5rem 0.9rem;
        font-size: 0.95rem;
        font-weight: 500;
        background-color: #0078d4;
        color: white;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        transition: background-color 0.2s, transform 0.1s;
      }

      .date-filter button:hover {
        background-color: #005a9e;
        transform: translateY(-1px);
      }

      .date-filter button:active {
        transform: translateY(0);
      }

      .date-filter button:nth-child(3) {
        background-color: #999;
      }

      .date-filter button:nth-child(3):hover {
        background-color: #666;
      }

      @media (max-width: 600px) {
        .date-filter {
          flex-direction: column;
          align-items: flex-start;
        }

        .date-filter input[type='date'] {
          width: 100%;
        }

        .date-filter button {
          width: 100%;
        }
      }
    `,
  ],
})
export class DateFilterComponent {
  @Output() filterChange = new EventEmitter<{
    startDate: string;
    endDate: string;
  }>();

  startDate: string = '';
  endDate: string = '';

  applyFilter() {
    if (this.startDate && this.endDate) {
      this.filterChange.emit({
        startDate: this.startDate,
        endDate: this.endDate,
      });
    } else {
      alert('Selecione ambas as datas!');
    }
  }

  clearFilter() {
    this.startDate = '';
    this.endDate = '';
    this.filterChange.emit({ startDate: '', endDate: '' });
  }
}
