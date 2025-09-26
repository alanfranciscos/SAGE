export interface Nurse {
  id: string;
  name: string;
  cpf: string;
  email: string;
  tel: string;
  token: string;
  status: 'active' | 'inactive';
  lastUse: string;
  registration: string;
}
