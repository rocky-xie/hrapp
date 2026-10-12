import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

export interface IDispatchAssignment {
  id?: number;
  candidateId?: number;
  personId?: number | null;
  partnerOrganizationId?: number;
  department?: string;
  positionId?: number | null;
  startDate?: Date | null;
  expectedEndDate?: Date | null;
  observationDays?: number | null;
  status?: string;
  finalDecision?: string | null;
  note?: string | null;
  application?: IRecruitmentApplication | null;
}

export class DispatchAssignment implements IDispatchAssignment {
  constructor(
    public id?: number,
    public candidateId?: number,
    public personId?: number | null,
    public partnerOrganizationId?: number,
    public department?: string,
    public positionId?: number | null,
    public startDate?: Date | null,
    public expectedEndDate?: Date | null,
    public observationDays?: number | null,
    public status?: string,
    public finalDecision?: string | null,
    public note?: string | null,
    public application?: IRecruitmentApplication | null,
  ) {}
}
