export interface Control {
  id: string;
  controlId: number;
  residentId: string | null; // se null -> disponível
}
