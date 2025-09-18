export interface CreateResidentRequestDto {
  fullName: string;
  cpf: string;
  sex: string;
  birthDate: string;
  emergencyName?: string;
  emergencyPhone?: string;
  relationship?: string;
  residentialUnit: string;
  controlNumber: number;
  imageData?: string;
}

interface ResidentHeaderResponseDto {
  id: string;
  fullName: string;
  residentialUnit: string;
  imageData: string;
}

export interface ResidentDetailsResponseDto extends ResidentHeaderResponseDto {
  cpf: string;
  sex: string;
  birthDate: string;
  emergencyName?: string;
  emergencyPhone?: string;
  relationship?: string;
  controlNumber: number;
}

export interface ResidentListResponseDto {
  severalResidents: ResidentHeaderResponseDto[];
  warningResidents: ResidentHeaderResponseDto[];
  normalResidents: ResidentHeaderResponseDto[];
  totalResidents: number;
}
