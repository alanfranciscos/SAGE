// start-assist-dialog.component.ts
import { Component, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogModule,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-start-assist-dialog',
  standalone: true,
  imports: [
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  template: `
    <h2 mat-dialog-title>Iniciar Atendimento</h2>
    <mat-dialog-content>
      <p>Digite o token do cuidador:</p>
      <mat-form-field appearance="outline" class="w-full">
        <input matInput [(ngModel)]="caregiverToken" placeholder="Token" />
      </mat-form-field>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="cancel()">Cancelar</button>
      <button mat-raised-button color="primary" (click)="confirm()">
        Confirmar
      </button>
    </mat-dialog-actions>
  `,
})
export class StartAssistDialogComponent {
  caregiverToken: string = '';

  constructor(
    private dialogRef: MatDialogRef<StartAssistDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { assistId: string }
  ) {}

  cancel() {
    this.dialogRef.close();
  }

  confirm() {
    this.dialogRef.close(this.caregiverToken);
  }
}
