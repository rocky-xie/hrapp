import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';

export interface IPositionAssignment {
  id?: number;
  primaryOwner?: boolean;
  startDate?: Date | null;
  endDate?: Date | null;
  responsibilityScope?: string | null;
  active?: boolean;
  person?: IPerson;
  position?: IPosition;
}

export class PositionAssignment implements IPositionAssignment {
  constructor(
    public id?: number,
    public primaryOwner?: boolean,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public responsibilityScope?: string | null,
    public active?: boolean,
    public person?: IPerson,
    public position?: IPosition,
  ) {
    this.primaryOwner = this.primaryOwner ?? false;
    this.active = this.active ?? false;
  }
}
