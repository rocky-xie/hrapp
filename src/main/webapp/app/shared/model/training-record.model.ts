import { type TrainingType } from '@/shared/model/enumerations/training-type.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';
import { type ITrainingGoal } from '@/shared/model/training-goal.model';

export interface ITrainingRecord {
  id?: number;
  trainingDate?: Date;
  trainingType?: keyof typeof TrainingType;
  topic?: string;
  taskDescription?: string | null;
  resultDescription?: string | null;
  evidence?: string | null;
  nextAction?: string | null;
  person?: IPerson;
  trainingGoal?: ITrainingGoal | null;
  position?: IPosition | null;
  mentor?: IPerson | null;
}

export class TrainingRecord implements ITrainingRecord {
  constructor(
    public id?: number,
    public trainingDate?: Date,
    public trainingType?: keyof typeof TrainingType,
    public topic?: string,
    public taskDescription?: string | null,
    public resultDescription?: string | null,
    public evidence?: string | null,
    public nextAction?: string | null,
    public person?: IPerson,
    public trainingGoal?: ITrainingGoal | null,
    public position?: IPosition | null,
    public mentor?: IPerson | null,
  ) {}
}
