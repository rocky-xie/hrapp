import { type BackupStatus } from '@/shared/model/enumerations/backup-status.model';
import { type DocumentStatus } from '@/shared/model/enumerations/document-status.model';
import { type ImportanceLevel } from '@/shared/model/enumerations/importance-level.model';
import { type RiskLevel } from '@/shared/model/enumerations/risk-level.model';
import { type RiskType } from '@/shared/model/enumerations/risk-type.model';
import { type IKeyResponsibilityCategory } from '@/shared/model/key-responsibility-category.model';
import { type IPosition } from '@/shared/model/position.model';
export interface IPositionRisk {
  id?: number;
  riskType?: keyof typeof RiskType;
  riskLevel?: keyof typeof RiskLevel;
  documentStatus?: keyof typeof DocumentStatus | null;
  backupStatus?: keyof typeof BackupStatus | null;
  customerOrSystemDependency?: keyof typeof ImportanceLevel | null;
  riskDescription?: string | null;
  improvementAction?: string | null;
  identifiedDate?: Date;
  targetDate?: Date | null;
  closedDate?: Date | null;
  position?: IPosition;
  category?: IKeyResponsibilityCategory | null;
}

export class PositionRisk implements IPositionRisk {
  constructor(
    public id?: number,
    public riskType?: keyof typeof RiskType,
    public riskLevel?: keyof typeof RiskLevel,
    public documentStatus?: keyof typeof DocumentStatus | null,
    public backupStatus?: keyof typeof BackupStatus | null,
    public customerOrSystemDependency?: keyof typeof ImportanceLevel | null,
    public riskDescription?: string | null,
    public improvementAction?: string | null,
    public identifiedDate?: Date,
    public targetDate?: Date | null,
    public closedDate?: Date | null,
    public position?: IPosition,
    public category?: IKeyResponsibilityCategory | null,
  ) {}
}
