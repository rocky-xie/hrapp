import { type DocumentStatus } from '@/shared/model/enumerations/document-status.model';
import { type ImportanceLevel } from '@/shared/model/enumerations/importance-level.model';
import { type ReadinessLevel } from '@/shared/model/enumerations/readiness-level.model';
import { type RiskLevel } from '@/shared/model/enumerations/risk-level.model';
import { type IPosition } from '@/shared/model/position.model';
export interface IPositionRiskEvaluation {
  id?: number;
  evaluationDate?: Date;
  ownerCount?: number | null;
  substitutableOwnerCount?: number | null;
  hasSubstitute?: boolean;
  documentStatus?: keyof typeof DocumentStatus | null;
  customerOrSystemDependency?: keyof typeof ImportanceLevel | null;
  successionReadiness?: keyof typeof ReadinessLevel | null;
  riskLevel?: keyof typeof RiskLevel;
  riskReason?: string | null;
  recommendedAction?: string | null;
  position?: IPosition;
}

export class PositionRiskEvaluation implements IPositionRiskEvaluation {
  constructor(
    public id?: number,
    public evaluationDate?: Date,
    public ownerCount?: number | null,
    public substitutableOwnerCount?: number | null,
    public hasSubstitute?: boolean,
    public documentStatus?: keyof typeof DocumentStatus | null,
    public customerOrSystemDependency?: keyof typeof ImportanceLevel | null,
    public successionReadiness?: keyof typeof ReadinessLevel | null,
    public riskLevel?: keyof typeof RiskLevel,
    public riskReason?: string | null,
    public recommendedAction?: string | null,
    public position?: IPosition,
  ) {
    this.hasSubstitute = this.hasSubstitute ?? false;
  }
}
