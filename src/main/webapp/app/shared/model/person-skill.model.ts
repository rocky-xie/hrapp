import { type ConfidenceLevel } from '@/shared/model/enumerations/confidence-level.model';
import { type IPerson } from '@/shared/model/person.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';
import { type ISkill } from '@/shared/model/skill.model';

export interface IPersonSkill {
  id?: number;
  assessmentDate?: Date;
  nextReviewDate?: Date | null;
  evidence?: string | null;
  confidence?: keyof typeof ConfidenceLevel | null;
  growthDirection?: string | null;
  person?: IPerson;
  skill?: ISkill;
  currentLevel?: ISkillLevel;
  previousLevel?: ISkillLevel | null;
}

export class PersonSkill implements IPersonSkill {
  constructor(
    public id?: number,
    public assessmentDate?: Date,
    public nextReviewDate?: Date | null,
    public evidence?: string | null,
    public confidence?: keyof typeof ConfidenceLevel | null,
    public growthDirection?: string | null,
    public person?: IPerson,
    public skill?: ISkill,
    public currentLevel?: ISkillLevel,
    public previousLevel?: ISkillLevel | null,
  ) {}
}
