import {
  Component,
  Input,
  Output,
  EventEmitter,
  forwardRef,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ControlValueAccessor,
  NG_VALUE_ACCESSOR,
  FormsModule,
} from '@angular/forms';

export interface SelectOption {
  value: string | number;
  label: string;
  disabled?: boolean;
}

@Component({
  selector: 'app-select-input',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './select-input.component.html',
  styleUrl: './select-input.component.scss',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectInputComponent),
      multi: true,
    },
  ],
})
export class SelectInputComponent implements ControlValueAccessor {
  @Input() label: string = '';
  @Input() id: string = '';
  @Input() name: string = '';
  @Input() options: SelectOption[] = [];
  @Input() placeholder: string = 'Selecione uma opção...';
  @Input() required: boolean = false;
  @Input() disabled: boolean = false;

  @Output() valueChange = new EventEmitter<string | number>();

  onChange: (value: any) => void = () => {};
  onTouched: () => void = () => {};

  private _value: string | number | null = null;

  get value(): string | number | null {
    return this._value;
  }

  @Input()
  set value(val: string | number | null) {
    if (val !== this._value) {
      this._value = val;
      this.onChange(val);
      this.valueChange.emit(val as string | number);
    }
  }

  writeValue(value: any): void {
    this.value = value;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onSelectionChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.value = target.value === '' ? null : target.value;
  }

  onBlur(): void {
    this.onTouched();
  }
}
