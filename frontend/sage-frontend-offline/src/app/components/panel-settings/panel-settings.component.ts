import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from '../button/button.component';
import { FormsModule } from '@angular/forms';
import { RegexHexaDirective } from '../../directive/regex-hexa.directive';
import { ToastrService } from 'ngx-toastr';
import { SettingsService } from '../../controller/settings/settings.service';

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
  @Input() panel_model: string = 'Indefinido';
  @Input() status: boolean = true;
  @Input() ip_address: string = 'Indefinido';
  @Input() mac_address: string = 'Indefinido';
  @Input() account: string = 'Indefinido';
  @Input() serial_number: string = 'Indefinido';
  portNumber: string = '';

  constructor(private toastr: ToastrService,
    private settingsService: SettingsService,
  ) { }

  async ngOnInit(): Promise<void> {
    const settingsDetails = await this.settingsService.getPanelSettingsBySerialNumber(
      "2785040674"
    );

    console.log(settingsDetails.model === null);

    this.serial_number = settingsDetails.serialNumber;

    this.status = settingsDetails.status === 'Online' ? true : false;

    this.panel_model = settingsDetails.model === null ? "Indefinido" : settingsDetails.model;

    this.panel_model = settingsDetails.model === null ? "Indefinido" : settingsDetails.model;

    this.ip_address = settingsDetails.ipAddress === null ? "Indefinido" : settingsDetails.ipAddress;

    this.mac_address = settingsDetails.macAddress === null ? "Indefinido" : settingsDetails.macAddress;

    this.account = settingsDetails.account === null ? "Indefinido" : settingsDetails.account;

    console.log(settingsDetails);

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
