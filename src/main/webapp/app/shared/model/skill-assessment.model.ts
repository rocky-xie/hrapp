import { type AssessmentResult } from '@/shared/model/enumerations/assessment-result.model';
import { type IPerson } from '@/shared/model/person.model';
import { type ISkill } from '@/shared/model/skill.model';
import { type ISkillLevel } from '@/shared/model/skill-level.model';

export interface ISkillAssessment {
  id?: number;
  assessmentDate?: Date;
  result?: keyof typeof AssessmentResult;
  evidence?: string | null;
  comment?: string | null;
  person?: IPerson | null;
  skill?: ISkill | null;
  assessor?: IPerson | null;
  newLevel?: ISkillLevel | null;
}

export class SkillAssessment implements ISkillAssessment {
  constructor(
    public id?: number,
    public assessmentDate?: Date,
    public result?: keyof typeof AssessmentResult,
    public evidence?: string | null,
    public comment?: string | null,
    public person?: IPerson | null,
    public skill?: ISkill | null,
    public assessor?: IPerson | null,
    public newLevel?: ISkillLevel | null,
  ) {}
}
