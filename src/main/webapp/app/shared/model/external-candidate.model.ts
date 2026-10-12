export interface IExternalCandidate {
  id?: number;
  fullName?: string;
  nameReading?: string | null;
  englishName?: string | null;
  birthDate?: Date | null;
  gender?: string | null;
  phone?: string | null;
  email?: string | null;
  address?: string | null;
  educationSummary?: string | null;
  workSummary?: string | null;
  convertedPersonId?: number | null;
  createdAt?: Date;
  updatedAt?: Date | null;
}

export class ExternalCandidate implements IExternalCandidate {
  constructor(
    public id?: number,
    public fullName?: string,
    public nameReading?: string | null,
    public englishName?: string | null,
    public birthDate?: Date | null,
    public gender?: string | null,
    public phone?: string | null,
    public email?: string | null,
    public address?: string | null,
    public educationSummary?: string | null,
    public workSummary?: string | null,
    public convertedPersonId?: number | null,
    public createdAt?: Date,
    public updatedAt?: Date | null,
  ) {}
}
