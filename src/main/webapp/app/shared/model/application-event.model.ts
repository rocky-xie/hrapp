import { type ApplicationStageType } from '@/shared/model/enumerations/application-stage-type.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

export interface IApplicationEvent {
  id?: number;
  stageType?: keyof typeof ApplicationStageType | null;
  eventType?: string;
  previousValue?: string | null;
  newValue?: string | null;
  actor?: string;
  occurredAt?: Date;
  reason?: string | null;
  metadataJson?: string | null;
  application?: IRecruitmentApplication | null;
}

export class ApplicationEvent implements IApplicationEvent {
  constructor(
    public id?: number,
    public stageType?: keyof typeof ApplicationStageType | null,
    public eventType?: string,
    public previousValue?: string | null,
    public newValue?: string | null,
    public actor?: string,
    public occurredAt?: Date,
    public reason?: string | null,
    public metadataJson?: string | null,
    public application?: IRecruitmentApplication | null,
  ) {}
}
