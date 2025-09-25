import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-panel-settings',
  standalone: true,
  imports: [
    CommonModule,
  ],
  templateUrl: './panel-settings.component.html',
  styleUrl: './panel-settings.component.scss'
})
export class PanelSettingsComponent implements OnInit {
  @Input() panel_model: string = 'Active Full 32';
  @Input() status: string = 'Online';
  @Input() ip_address: string = '192.168.1.100';
  @Input() mac_address: string = '00:1A:2B:3C:4D:5E';
  @Input() account: string = '0001';
  @Input() serial_number: string = '0123456789';
  
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  private iconMap: Record<string, string> ={
    title:'fa-solid fa-desktop',
    model:'fa-solid fa-microchip',
    status:'fa-solid fa-circle',
    ip_address:'fa-solid fa-globe',
    mac_address:'fa-solid fa-diagram-project',
    account:'fa-solid fa-inbox',
    serial_number:'fa-solid fa-qrcode',
    notice:'fa-solid fa-circle-info',
  }

  getIconClass(item: string): string {
    return this.iconMap[item] || 'fa-solid fa-circle-question';
  }

}
