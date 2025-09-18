import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { UntypedFormBuilder } from '@angular/forms';
import { ButtonComponent } from '../button/button.component';

@Component({
  selector: 'app-image-input',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonComponent],
  templateUrl: './image-input.component.html',
  styleUrl: './image-input.component.scss',
})
export class ImageInputComponent {
  private defaultImage: string = '/assets/default/profile.png';
  @Input() image: string | null | File = this.defaultImage;
  showImage: string = this.defaultImage;

  @Output() imageChange = new EventEmitter<File | null>();

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.image = file;

      const reader = new FileReader();
      reader.onload = (e: ProgressEvent<FileReader>) => {
        const imageData = e.target?.result as string;
        this.showImage = imageData;
      };

      reader.readAsDataURL(file);
      this.imageChange.emit(file);
    }
  }

  onRemoveImage() {
    this.image = this.defaultImage;
    this.showImage = this.defaultImage;
    this.imageChange.emit(null);
  }
}
