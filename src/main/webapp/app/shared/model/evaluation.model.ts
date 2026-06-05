import { type AssessmentResult } from '@/shared/model/enumerations/assessment-result.model';
import { type ProgressStatus } from '@/shared/model/enumerations/progress-status.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';
import { type ITrainingGoal } from '@/shared/model/training-goal.model';

export interface IEvaluation {
  id?: number;
  evaluationName?: string;
  evaluationDate?: Date;
  periodLabel?: string | null;
  progressStatus?: keyof typeof ProgressStatus | null;
  result?: keyof typeof AssessmentResult | null;
  strengths?: string | null;
  weaknesses?: string | null;
  supportNeeded?: string | null;
  nextTrainingFocus?: string | null;
  positionAdjustmentNeeded?: boolean | null;
  person?: IPerson;
  position?: IPosition | null;
  trainingGoal?: ITrainingGoal | null;
  evaluator?: IPerson | null;
}

export class Evaluation implements IEvaluation {
  constructor(
    public id?: number,
    public evaluationName?: string,
    public evaluationDate?: Date,
    public periodLabel?: string | null,
    public progressStatus?: keyof typeof ProgressStatus | null,
    public result?: keyof typeof AssessmentResult | null,
    public strengths?: string | null,
    public weaknesses?: string | null,
    public supportNeeded?: string | null,
    public nextTrainingFocus?: string | null,
    public positionAdjustmentNeeded?: boolean | null,
    public person?: IPerson,
    public position?: IPosition | null,
    public trainingGoal?: ITrainingGoal | null,
    public evaluator?: IPerson | null,
  ) {
    this.positionAdjustmentNeeded = this.positionAdjustmentNeeded ?? false;
  }
}
