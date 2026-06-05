import { type ImportanceLevel } from '@/shared/model/enumerations/importance-level.model';
import { type PositionType } from '@/shared/model/enumerations/position-type.model';
import { type ReviewCycle } from '@/shared/model/enumerations/review-cycle.model';
export interface IPosition {
  id?: number;
  positionCode?: string;
  positionName?: string;
  positionType?: keyof typeof PositionType;
  businessImportance?: keyof typeof ImportanceLevel;
  keyPosition?: boolean;
  description?: string | null;
  plannedHeadcount?: number | null;
  minimumOwnerCount?: number | null;
  reviewCycle?: keyof typeof ReviewCycle | null;
  active?: boolean;
}

export class Position implements IPosition {
  constructor(
    public id?: number,
    public positionCode?: string,
    public positionName?: string,
    public positionType?: keyof typeof PositionType,
    public businessImportance?: keyof typeof ImportanceLevel,
    public keyPosition?: boolean,
    public description?: string | null,
    public plannedHeadcount?: number | null,
    public minimumOwnerCount?: number | null,
    public reviewCycle?: keyof typeof ReviewCycle | null,
    public active?: boolean,
  ) {
    this.keyPosition = this.keyPosition ?? false;
    this.active = this.active ?? false;
  }
}
