import { Component, Input } from '@angular/core';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-tooltip-wrapper',
  standalone: true,
  imports: [CommonModule, MatTooltipModule],
  templateUrl: './tooltip-wrapper.component.html',
  styleUrl: './tooltip-wrapper.component.scss',
})
export class TooltipWrapperComponent {
  @Input() tooltip: string = '';
  @Input() position: 'above' | 'below' | 'left' | 'right' = 'above';
@Input() showDelay: number = 0;
@Input() hideDelay: number = 0;  
}