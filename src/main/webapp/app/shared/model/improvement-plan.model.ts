import { type PlanStatus } from '@/shared/model/enumerations/plan-status.model';
import { type IPosition } from '@/shared/model/position.model';
import { type ISkill } from '@/shared/model/skill.model';

export interface IImprovementPlan {
  id?: number;
  planName?: string;
  planStatus?: keyof typeof PlanStatus;
  problemSummary?: string | null;
  improvementAction?: string | null;
  ownerName?: string | null;
  startDate?: Date | null;
  targetDate?: Date | null;
  completionDate?: Date | null;
  reviewResult?: string | null;
  position?: IPosition | null;
  skill?: ISkill | null;
}

export class ImprovementPlan implements IImprovementPlan {
  constructor(
    public id?: number,
    public planName?: string,
    public planStatus?: keyof typeof PlanStatus,
    public problemSummary?: string | null,
    public improvementAction?: string | null,
    public ownerName?: string | null,
    public startDate?: Date | null,
    public targetDate?: Date | null,
    public completionDate?: Date | null,
    public reviewResult?: string | null,
    public position?: IPosition | null,
    public skill?: ISkill | null,
  ) {}
}
