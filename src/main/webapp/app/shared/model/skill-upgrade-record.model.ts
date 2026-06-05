import { type SkillChangeType } from '@/shared/model/enumerations/skill-change-type.model';
import { type IPerson } from '@/shared/model/person.model';
import { type ISkill } from '@/shared/model/skill.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';

export interface ISkillUpgradeRecord {
  id?: number;
  changeType?: keyof typeof SkillChangeType;
  changeDate?: Date;
  reason?: string;
  beforeLevelLabel?: string | null;
  afterLevelLabel?: string | null;
  evidence?: string | null;
  comment?: string | null;
  person?: IPerson;
  skill?: ISkill;
  oldLevel?: ISkillLevel | null;
  newLevel?: ISkillLevel;
  assessor?: IPerson | null;
}

export class SkillUpgradeRecord implements ISkillUpgradeRecord {
  constructor(
    public id?: number,
    public changeType?: keyof typeof SkillChangeType,
    public changeDate?: Date,
    public reason?: string,
    public beforeLevelLabel?: string | null,
    public afterLevelLabel?: string | null,
    public evidence?: string | null,
    public comment?: string | null,
    public person?: IPerson,
    public skill?: ISkill,
    public oldLevel?: ISkillLevel | null,
    public newLevel?: ISkillLevel,
    public assessor?: IPerson | null,
  ) {}
}
