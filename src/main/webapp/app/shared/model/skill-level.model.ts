import { type LevelCode } from '@/shared/model/enumerations/level-code.model';
export interface ISkillLevel {
  id?: number;
  code?: keyof typeof LevelCode;
  levelName?: string;
  definition?: string | null;
  observableEvidence?: string | null;
  sortOrder?: number;
}

export class SkillLevel implements ISkillLevel {
  constructor(
    public id?: number,
    public code?: keyof typeof LevelCode,
    public levelName?: string,
    public definition?: string | null,
    public observableEvidence?: string | null,
    public sortOrder?: number,
  ) {}
}
