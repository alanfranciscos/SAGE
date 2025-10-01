import { Component, OnInit } from '@angular/core';
import { ButtonComponent } from '../../../components/button/button.component';
import { Router, RouterLink } from '@angular/router';
import { CommonModule, NgClass } from '@angular/common';
import { TooltipWrapperComponent } from '../../../components/tooltip-wrapper/tooltip-wrapper.component';
import { ResidentService } from '../../../controller/resident/resident.service';
import { SseService } from '../../../controller/sse/sse.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, TooltipWrapperComponent, NgClass, CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
})
export class SidebarComponent implements OnInit {
  activeItem: string = '';
  links: Map<string, string> = new Map<string, string>([
    ['dashboard', '/'],
    ['settings', '/settings'],
    ['profile', '/profile'],
  ]);

  constructor(
    private router: Router,
    private residentService: ResidentService,
    private sseService: SseService
  ) {}
  totalActiveCalls: number = 0;

  private iconMap: Record<string, string> = {
    dashboard: 'fa-solid fa-chart-line',
    alerts: 'fa-solid fa-bell',
    reports: 'fa-solid fa-file-lines',
    settings: 'fa-solid fa-gear',
    nurse: 'fa-solid fa-user-nurse',
  };

  getIconClass(item: string): string {
    return this.iconMap[item] || 'fa-solid fa-circle-question';
  }

  async ngOnInit(): Promise<void> {
    this.activeItem = this.router.url;
    this.totalActiveCalls =
      await this.residentService.getTotalActiveResidentsCalls();
    this.residentService.totalActiveCalls$.subscribe((total) => {
      this.totalActiveCalls = total;
    });

    this.sseService.messages$.subscribe(async (msg) => {
      if (!msg) return;


      this.totalActiveCalls =
      await this.residentService.getTotalActiveResidentsCalls();
      this.residentService.totalActiveCalls$.subscribe((total) => {
        this.totalActiveCalls = total;
      });
    });
  }

  setActiveItem(item: string) {
    this.activeItem = this.links!.get(item) || '/';
  }

  getActiveRoute(): string {
    return this.router.url;
  }
}
