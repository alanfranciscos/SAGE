import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-search-input',
  templateUrl: './search-input.component.html',
  styleUrls: ['./search-input.component.scss'],
  standalone: true,
  imports: [FormsModule],
})
export class SearchInputComponent {
  @Input() title: string = '';
  @Output() search: EventEmitter<string> = new EventEmitter<string>();

  @Input() placeholder: string = 'Digite para pesquisar...';
  @Input() searchTerm!: string;

  onSearch(): void {
    // Emite o termo de pesquisa atual
    this.search.emit(this.searchTerm);
  }

  // Opcional: Adicionar pesquisa ao pressionar Enter
  onEnterPress(event: KeyboardEvent): void {
    if (event.key === 'Enter') {
      this.onSearch();
    }
  }
}
