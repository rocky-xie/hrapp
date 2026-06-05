import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';

export interface IStaffSubstitution {
  id?: number;
  coverageRate?: number;
  thresholdRate?: number;
  totalSkillCount?: number | null;
  coveredSkillCount?: number | null;
  missingSkills?: string | null;
  substitutable?: boolean;
  evaluationDate?: Date;
  reason?: string | null;
  reviewDate?: Date | null;
  expiryDate?: Date | null;
  position?: IPosition;
  candidatePerson?: IPerson;
}

export class StaffSubstitution implements IStaffSubstitution {
  constructor(
    public id?: number,
    public coverageRate?: number,
    public thresholdRate?: number,
    public totalSkillCount?: number | null,
    public coveredSkillCount?: number | null,
    public missingSkills?: string | null,
    public substitutable?: boolean,
    public evaluationDate?: Date,
    public reason?: string | null,
    public reviewDate?: Date | null,
    public expiryDate?: Date | null,
    public position?: IPosition,
    public candidatePerson?: IPerson,
  ) {
    this.substitutable = this.substitutable ?? false;
  }
}
