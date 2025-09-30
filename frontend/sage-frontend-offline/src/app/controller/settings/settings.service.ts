import { Injectable } from '@angular/core';
import { ApiService } from '../../services/api/api.service';
import { SettingsDto } from '../../model/Settings';

@Injectable({
    providedIn: 'root',
})
export class SettingsService {
    api: ReturnType<ApiService['getApi']>;

    constructor(private apiService: ApiService) {
        this.api = this.apiService.getApi();
    }

    async getPanelSettingsBySerialNumber(
        serialNumber: string
    ): Promise<SettingsDto> {
        const response = await this.api.get<SettingsDto>(
            `http://localhost:8080/api/v1/alarms/serial/${serialNumber}`
        );

        if (response.status !== 200) {
            throw new Error('Failed to fetch settings details');
        }

        // console.log(response.data);

        return response.data;
    }
}