import { type PlanStatus } from '@/shared/model/enumerations/plan-status.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';
import { type ISkill } from '@/shared/model/skill.model';

export interface ITrainingGoal {
  id?: number;
  goalName?: string;
  goalDescription?: string | null;
  targetLevelDescription?: string | null;
  startDate?: Date | null;
  targetDate?: Date | null;
  status?: keyof typeof PlanStatus;
  person?: IPerson | null;
  position?: IPosition | null;
  skill?: ISkill | null;
  targetLevel?: ISkillLevel | null;
}

export class TrainingGoal implements ITrainingGoal {
  constructor(
    public id?: number,
    public goalName?: string,
    public goalDescription?: string | null,
    public targetLevelDescription?: string | null,
    public startDate?: Date | null,
    public targetDate?: Date | null,
    public status?: keyof typeof PlanStatus,
    public person?: IPerson | null,
    public position?: IPosition | null,
    public skill?: ISkill | null,
    public targetLevel?: ISkillLevel | null,
  ) {}
}
