import { type EmploymentStatus } from '@/shared/model/enumerations/employment-status.model';
import { type Gender } from '@/shared/model/enumerations/gender.model';
export interface IPerson {
  id?: number;
  employeeCode?: string | null;
  personName?: string;
  age?: number | null;
  gender?: keyof typeof Gender | null;
  department?: string | null;
  currentRole?: string | null;
  employmentStatus?: keyof typeof EmploymentStatus;
  joinDate?: Date | null;
  mentorFlag?: boolean;
  coreCandidateFlag?: boolean;
  note?: string | null;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public employeeCode?: string | null,
    public personName?: string,
    public age?: number | null,
    public gender?: keyof typeof Gender | null,
    public department?: string | null,
    public currentRole?: string | null,
    public employmentStatus?: keyof typeof EmploymentStatus,
    public joinDate?: Date | null,
    public mentorFlag?: boolean,
    public coreCandidateFlag?: boolean,
    public note?: string | null,
  ) {
    this.mentorFlag = this.mentorFlag ?? false;
    this.coreCandidateFlag = this.coreCandidateFlag ?? false;
  }
}
