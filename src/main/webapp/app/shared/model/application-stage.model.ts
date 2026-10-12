import { type ApplicationStageType } from '@/shared/model/enumerations/application-stage-type.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

export interface IApplicationStage {
  id?: number;
  stageType?: keyof typeof ApplicationStageType;
  sequenceNumber?: number;
  stageStatus?: string;
  startedAt?: Date | null;
  completedAt?: Date | null;
  selected?: boolean | null;
  decisionReason?: string | null;
  application?: IRecruitmentApplication | null;
}

export class ApplicationStage implements IApplicationStage {
  constructor(
    public id?: number,
    public stageType?: keyof typeof ApplicationStageType,
    public sequenceNumber?: number,
    public stageStatus?: string,
    public startedAt?: Date | null,
    public completedAt?: Date | null,
    public selected?: boolean | null,
    public decisionReason?: string | null,
    public application?: IRecruitmentApplication | null,
  ) {
    this.selected = this.selected ?? false;
  }
}
