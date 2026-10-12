import { type ApplicationDecision } from '@/shared/model/enumerations/application-decision.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

export interface IHiringDecision {
  id?: number;
  decision?: keyof typeof ApplicationDecision;
  decisionSource?: string;
  decidedBy?: string;
  decidedAt?: Date;
  reason?: string | null;
  correctionOfId?: number | null;
  active?: boolean;
  application?: IRecruitmentApplication | null;
}

export class HiringDecision implements IHiringDecision {
  constructor(
    public id?: number,
    public decision?: keyof typeof ApplicationDecision,
    public decisionSource?: string,
    public decidedBy?: string,
    public decidedAt?: Date,
    public reason?: string | null,
    public correctionOfId?: number | null,
    public active?: boolean,
    public application?: IRecruitmentApplication | null,
  ) {
    this.active = this.active ?? false;
  }
}
