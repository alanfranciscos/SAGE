import { Component } from '@angular/core';
import { ButtonComponent } from '../../../components/button/button.component';
import { Router, RouterLink } from '@angular/router';
import { NgClass } from '@angular/common';
import { TooltipWrapperComponent } from "../../../components/tooltip-wrapper/tooltip-wrapper.component";

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [ButtonComponent, RouterLink, TooltipWrapperComponent,NgClass],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
})
export class SidebarComponent {
  activeItem: string = '';
  links: Map<string, string> = new Map<string, string>([
    ['dashboard', '/'],
    ['settings', '/settings'],
    ['profile', '/profile'],
  ]);

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.activeItem = this.router.url;
  }

  setActiveItem(item: string) {
    this.activeItem = this.links!.get(item) || '/';
  }

  getActiveRoute(): string {
    return this.router.url;
  }
}
