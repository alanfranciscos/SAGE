import { Directive, HostListener } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[appRegexHexa]',
  standalone: true
})
export class RegexHexaDirective {

  constructor(private control: NgControl) {}

  @HostListener('input', ['$event'])
  onInput(event: Event) {
    const input = event.target as HTMLInputElement;
    let cleaned = input.value.replace(/[^0-9a-fA-F]/g, '').toUpperCase();

    if (cleaned !== input.value) {
      input.value = cleaned;

      this.control.control?.setValue(cleaned);
    }
  }

}
