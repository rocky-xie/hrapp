import { type RequirementImportance } from '@/shared/model/enumerations/requirement-importance.model';
import { type IPosition } from '@/shared/model/position.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';
import { type ISkill } from '@/shared/model/skill.model';

export interface IPositionSkillRequirement {
  id?: number;
  importance?: keyof typeof RequirementImportance;
  remark?: string | null;
  certificationRequired?: boolean | null;
  position?: IPosition;
  skill?: ISkill;
  requiredLevel?: ISkillLevel;
  preferredLevel?: ISkillLevel | null;
}

export class PositionSkillRequirement implements IPositionSkillRequirement {
  constructor(
    public id?: number,
    public importance?: keyof typeof RequirementImportance,
    public remark?: string | null,
    public certificationRequired?: boolean | null,
    public position?: IPosition,
    public skill?: ISkill,
    public requiredLevel?: ISkillLevel,
    public preferredLevel?: ISkillLevel | null,
  ) {}
}
