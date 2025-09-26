import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from '../button/button.component';
import { FormsModule } from '@angular/forms';
import { RegexHexaDirective } from '../../directive/regex-hexa.directive';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-panel-settings',
  standalone: true,
  imports: [
    CommonModule,
    ButtonComponent,
    FormsModule,
    RegexHexaDirective
  ],
  templateUrl: './panel-settings.component.html',
  styleUrl: './panel-settings.component.scss'
})
export class PanelSettingsComponent implements OnInit {
  @Input() panel_model: string = 'Active Full 32';
  @Input() status: boolean = true;
  @Input() ip_address: string = '192.168.1.100';
  @Input() mac_address: string = '00:1A:2B:3C:4D:5E';
  @Input() account: string = '0001';
  @Input() serial_number: string = '0123456789';
  portNumber: string = '';

  constructor(private toastr: ToastrService) { }

  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  private iconMap: Record<string, string> = {
    title: 'fa-solid fa-desktop',
    model: 'fa-solid fa-microchip',
    status: 'fa-solid fa-circle',
    ip_address: 'fa-solid fa-globe',
    mac_address: 'fa-solid fa-diagram-project',
    account: 'fa-solid fa-inbox',
    serial_number: 'fa-solid fa-qrcode',
    notice: 'fa-solid fa-circle-info',
    port: 'fa-solid fa-ethernet',
  }

  getIconClass(item: string): string {
    return this.iconMap[item] || 'fa-solid fa-circle-question';
  }

  sendPort(portNumber: string): void {
    if (portNumber && portNumber.length === 4) {
      console.log('Porta enviada:', portNumber);
      this.toastr.success(`Porta ${portNumber} enviada com sucesso!`);
    } else {
      this.toastr.error('Porta inválida! Deve conter exatamente 4 caracteres hexadecimais.');
      console.log('Porta vazia!');
    }

  }

}
