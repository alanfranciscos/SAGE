export interface CreateCaregiverLeaderRequestDto {
  fullName: string;
  phone: string;
  email: string;
  cpf: string;
  caregiver_password: string;
  imageData?: string;
}

export interface CaregiverLeaderHeaderResponseDto {
  id: string;
  fullName: string;
  phone: string;
  email: string;
  imageData: string;
}
