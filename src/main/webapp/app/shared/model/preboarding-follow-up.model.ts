import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

export interface IPreboardingFollowUp {
  id?: number;
  candidateId?: number;
  personId?: number | null;
  plannedAt?: Date | null;
  contactedAt?: Date | null;
  ownerUsername?: string;
  contactMethod?: string | null;
  result?: string | null;
  candidateIntent?: string | null;
  riskLevel?: string | null;
  nextAction?: string | null;
  nextContactAt?: Date | null;
  application?: IRecruitmentApplication | null;
}

export class PreboardingFollowUp implements IPreboardingFollowUp {
  constructor(
    public id?: number,
    public candidateId?: number,
    public personId?: number | null,
    public plannedAt?: Date | null,
    public contactedAt?: Date | null,
    public ownerUsername?: string,
    public contactMethod?: string | null,
    public result?: string | null,
    public candidateIntent?: string | null,
    public riskLevel?: string | null,
    public nextAction?: string | null,
    public nextContactAt?: Date | null,
    public application?: IRecruitmentApplication | null,
  ) {}
}
