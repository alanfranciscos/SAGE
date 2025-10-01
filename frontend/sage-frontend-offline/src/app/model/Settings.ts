export interface SettingsDto {
  id: string;
  model: string;
  status: 'Online' | 'Offline' | null;
  ipAddress: string;
  macAddress: string;
  account: string;
  serialNumber: string;
  port: string;
}

export interface UpdateAlarmPortRequest {
  port: number;
}