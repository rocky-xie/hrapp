import { type ReadinessLevel } from '@/shared/model/enumerations/readiness-level.model';
import { type RiskLevel } from '@/shared/model/enumerations/risk-level.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';
export interface ISuccessionCandidate {
  id?: number;
  successionReadiness?: keyof typeof ReadinessLevel;
  requiredTraining?: string | null;
  estimatedTimeToReady?: string | null;
  riskAfterTraining?: keyof typeof RiskLevel | null;
  reviewDate?: Date | null;
  priority?: number | null;
  position?: IPosition;
  currentOwner?: IPerson | null;
  candidate?: IPerson;
}

export class SuccessionCandidate implements ISuccessionCandidate {
  constructor(
    public id?: number,
    public successionReadiness?: keyof typeof ReadinessLevel,
    public requiredTraining?: string | null,
    public estimatedTimeToReady?: string | null,
    public riskAfterTraining?: keyof typeof RiskLevel | null,
    public reviewDate?: Date | null,
    public priority?: number | null,
    public position?: IPosition,
    public currentOwner?: IPerson | null,
    public candidate?: IPerson,
  ) {}
}
