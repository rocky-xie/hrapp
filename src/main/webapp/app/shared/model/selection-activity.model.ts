import { type ApplicationStageType } from '@/shared/model/enumerations/application-stage-type.model';
import { type SelectionActivityType } from '@/shared/model/enumerations/selection-activity-type.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';
export interface ISelectionActivity {
  id?: number;
  stageType?: keyof typeof ApplicationStageType;
  activityType?: keyof typeof SelectionActivityType;
  startAt?: Date;
  endAt?: Date;
  meetingRoomId?: number | null;
  location?: string | null;
  status?: string;
  scheduleVersion?: number;
  changeReason?: string | null;
  externalCalendarProvider?: string | null;
  externalEventId?: string | null;
  application?: IRecruitmentApplication | null;
}

export class SelectionActivity implements ISelectionActivity {
  constructor(
    public id?: number,
    public stageType?: keyof typeof ApplicationStageType,
    public activityType?: keyof typeof SelectionActivityType,
    public startAt?: Date,
    public endAt?: Date,
    public meetingRoomId?: number | null,
    public location?: string | null,
    public status?: string,
    public scheduleVersion?: number,
    public changeReason?: string | null,
    public externalCalendarProvider?: string | null,
    public externalEventId?: string | null,
    public application?: IRecruitmentApplication | null,
  ) {}
}
