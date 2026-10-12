import { type ApplicationDecision } from '@/shared/model/enumerations/application-decision.model';
import { type ApplicationStageType } from '@/shared/model/enumerations/application-stage-type.model';
import { type ApplicationStatus } from '@/shared/model/enumerations/application-status.model';
import { type RecruitmentType } from '@/shared/model/enumerations/recruitment-type.model';
import { type IExternalCandidate } from '@/shared/model/external-candidate.model';
import { type IRecruitmentRequisitionVersion } from '@/shared/model/recruitment-requisition-version.model';
import { type IRecruitmentRequisition } from '@/shared/model/recruitment-requisition.model';

export interface IRecruitmentApplication {
  id?: number;
  applicationCode?: string;
  recruitmentType?: keyof typeof RecruitmentType;
  currentStage?: keyof typeof ApplicationStageType | null;
  status?: keyof typeof ApplicationStatus;
  decision?: keyof typeof ApplicationDecision | null;
  sourceType?: string;
  sourceDetail?: string | null;
  batchName?: string | null;
  appliedAt?: Date;
  closedAt?: Date | null;
  retentionUntil?: Date | null;
  convertedPersonId?: number | null;
  convertedAt?: Date | null;
  closeReason?: string | null;
  note?: string | null;
  candidate?: IExternalCandidate | null;
  requisition?: IRecruitmentRequisition | null;
  requisitionVersion?: IRecruitmentRequisitionVersion | null;
}

export class RecruitmentApplication implements IRecruitmentApplication {
  constructor(
    public id?: number,
    public applicationCode?: string,
    public recruitmentType?: keyof typeof RecruitmentType,
    public currentStage?: keyof typeof ApplicationStageType | null,
    public status?: keyof typeof ApplicationStatus,
    public decision?: keyof typeof ApplicationDecision | null,
    public sourceType?: string,
    public sourceDetail?: string | null,
    public batchName?: string | null,
    public appliedAt?: Date,
    public closedAt?: Date | null,
    public retentionUntil?: Date | null,
    public convertedPersonId?: number | null,
    public convertedAt?: Date | null,
    public closeReason?: string | null,
    public note?: string | null,
    public candidate?: IExternalCandidate | null,
    public requisition?: IRecruitmentRequisition | null,
    public requisitionVersion?: IRecruitmentRequisitionVersion | null,
  ) {}
}
