import { type RiskLevel } from '@/shared/model/enumerations/risk-level.model';
import { type RiskType } from '@/shared/model/enumerations/risk-type.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';

export interface IPersonRisk {
  id?: number;
  riskType?: keyof typeof RiskType;
  riskLevel?: keyof typeof RiskLevel;
  riskDescription?: string | null;
  improvementAction?: string | null;
  identifiedDate?: Date;
  targetDate?: Date | null;
  closedDate?: Date | null;
  person?: IPerson;
  position?: IPosition | null;
}

export class PersonRisk implements IPersonRisk {
  constructor(
    public id?: number,
    public riskType?: keyof typeof RiskType,
    public riskLevel?: keyof typeof RiskLevel,
    public riskDescription?: string | null,
    public improvementAction?: string | null,
    public identifiedDate?: Date,
    public targetDate?: Date | null,
    public closedDate?: Date | null,
    public person?: IPerson,
    public position?: IPosition | null,
  ) {}
}
