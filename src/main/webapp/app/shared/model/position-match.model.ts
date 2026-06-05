import { type ReadinessLevel } from '@/shared/model/enumerations/readiness-level.model';
import { type Recommendation } from '@/shared/model/enumerations/recommendation.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';

export interface IPositionMatch {
  id?: number;
  matchScore?: number | null;
  matchedSkills?: string | null;
  gapSkills?: string | null;
  readiness?: keyof typeof ReadinessLevel;
  recommendation?: keyof typeof Recommendation;
  analysisDate?: Date;
  remark?: string | null;
  person?: IPerson;
  position?: IPosition;
}

export class PositionMatch implements IPositionMatch {
  constructor(
    public id?: number,
    public matchScore?: number | null,
    public matchedSkills?: string | null,
    public gapSkills?: string | null,
    public readiness?: keyof typeof ReadinessLevel,
    public recommendation?: keyof typeof Recommendation,
    public analysisDate?: Date,
    public remark?: string | null,
    public person?: IPerson,
    public position?: IPosition,
  ) {}
}
