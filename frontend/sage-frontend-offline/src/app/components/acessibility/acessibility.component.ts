import { Component } from '@angular/core';
import { ButtonComponent } from '../button/button.component';
import { CommonModule } from '@angular/common';
import { AccessibilityService } from '../../controller/accessibility/accessibility.service';

@Component({
  selector: 'app-acessibility',
  standalone: true,
  imports: [ButtonComponent, CommonModule],
  templateUrl: './acessibility.component.html',
  styleUrls: ['./acessibility.component.scss'],
})
export class AcessibilityComponent {
  showModal = false;
  daltonicMode = false;

  constructor(private accessibilityService: AccessibilityService) {}

  ngOnInit() {
    this.accessibilityService.daltonicMode$.subscribe((enabled) => {
      this.daltonicMode = enabled;
    });
  }

  toggleModal() {
    this.showModal = !this.showModal;
  }

  toggleDaltonicMode() {
    this.accessibilityService.toggleDaltonicMode();

    if (this.accessibilityService.isDaltonicModeEnabled()) {
      document.body.classList.add('daltonic-mode');
    } else {
      document.body.classList.remove('daltonic-mode');
    }
  }
}
