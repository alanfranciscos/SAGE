import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [],
  templateUrl: './button.component.html',
  styleUrl: './button.component.scss',
})
export class ButtonComponent {
  @Input() label!: string;
  @Input() size: 'small' | 'medium' | 'large' = 'medium';
  @Input() type: 'primary' | 'secondary' | 'green' = 'primary';
  @Input() disabled: boolean = false;

  @Output() clicked = new EventEmitter<void>();

  onClick(): void {
    if (!this.disabled) {
      this.clicked.emit();
    }
  }
}
