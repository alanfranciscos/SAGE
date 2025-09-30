export interface CreateResidentRequestDto {
  fullName: string;
  cpf: string;
  sex: string;
  birthDate: string;
  emergencyName: string;
  emergencyPhone: string;
  relationship: string;
  residentialUnit: string;
  controlNumber: number;
  imageData?: string;
}

export interface ResidentHeaderResponseDto {
  id: string;
  fullName: string;
  residentialUnit: string;
  imageData: string;
}

export interface ResidentDetailsResponseDto extends ResidentHeaderResponseDto {
  id: string;
  fullName: string;
  cpf: string;
  sex: string;
  birthDate: string;
  age: number;
  residentialUnit: string;
  imageData: string;
  active: boolean;
  controlId: number;
  createdAt: string;
  updatedAt: string;
  emergencyFullName: string;
  emergencyPhone: string;
  emergencyRelationship: string;
}

export interface ResidentListResponseDto {
  severalResidents: ResidentHeaderResponseDto[];
  warningResidents: ResidentHeaderResponseDto[];
  normalResidents: ResidentHeaderResponseDto[];
  totalResidents: number;
}

export interface Resident {
  id: string;
  fullName: string;
  residentialUnit: string;
  imageData: string;
  severityLevel: 'emergency' | 'warning' | null;
  lastEndAt: string | null;
  callsLastDay: number;
}
