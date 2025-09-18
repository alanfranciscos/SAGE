import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonComponent } from '../../components/button/button.component';

type Steps = Record<number, string>;

@Component({
  selector: 'app-update',
  standalone: true,
  imports: [ButtonComponent],
  templateUrl: './update.component.html',
  styleUrl: './update.component.scss',
})
export class updateComponent {
  @Input() title: string = 'Atualização de Residente';
  @Input() steps!: string[];
  @Input() validStep: boolean = false;
  currentStep: number = 0;
  stepsNumber!: number[];

  @Output() updatedStep = new EventEmitter<number>();
  @Output() cancel = new EventEmitter<void>();
  @Output() finish = new EventEmitter<void>();

  ngOnInit() {
    this.stepsNumber = this.steps.map((_, index) => index + 1);
  }

  onNext() {
    if (this.currentStep < this.steps.length - 1 && this.validStep) {
      this.currentStep++;
      this.updatedStep.emit(this.currentStep);
    }
  }

  onPrevious() {
    if (this.currentStep > 0) {
      this.currentStep--;
      this.updatedStep.emit(this.currentStep);
    }
  }

  onCancel() {
    this.currentStep = 0;
    this.cancel.emit();
  }

  onFinish() {
    this.finish.emit();
  }
}
