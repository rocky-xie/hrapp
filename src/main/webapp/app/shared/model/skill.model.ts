import { type EvidenceType } from '@/shared/model/enumerations/evidence-type.model';
import { type SkillType } from '@/shared/model/enumerations/skill-type.model';
export interface ISkill {
  id?: number;
  skillCode?: string;
  skillName?: string;
  skillType?: keyof typeof SkillType;
  measurableFlag?: boolean;
  description?: string | null;
  evidenceType?: keyof typeof EvidenceType | null;
}

export class Skill implements ISkill {
  constructor(
    public id?: number,
    public skillCode?: string,
    public skillName?: string,
    public skillType?: keyof typeof SkillType,
    public measurableFlag?: boolean,
    public description?: string | null,
    public evidenceType?: keyof typeof EvidenceType | null,
  ) {
    this.measurableFlag = this.measurableFlag ?? false;
  }
}
