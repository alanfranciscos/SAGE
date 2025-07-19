import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonComponent } from '../../../components/button/button.component';
import { CommonModule } from '@angular/common';

type levelType = 'warning' | 'several' | 'normal';

@Component({
  selector: 'app-resident',
  standalone: true,
  imports: [ButtonComponent, CommonModule],
  templateUrl: './resident.component.html',
  styleUrl: './resident.component.scss',
})
export class ResidentComponent {
  private defaultImage: string = '/assets/default/profile.png';

  @Input() name!: string;
  @Input() room!: string;
  @Input() id!: string;
  @Input() level!: levelType;
  @Input() image: string | null | File = this.defaultImage;

  levelStyle!: string;
  showImage: string = this.defaultImage;

  @Output() showDetails = new EventEmitter<string>();

  ngOnInit() {
    this.setAlertStyle();
  }

  private setAlertStyle() {
    switch (this.level) {
      case 'warning':
        this.levelStyle = 'alert-warning';
        break;
      case 'several':
        this.levelStyle = 'alert-several';
        break;
    }
  }
}
