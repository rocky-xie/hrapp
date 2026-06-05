import { type CandidateJudgement } from '@/shared/model/enumerations/candidate-judgement.model';
import { type ImportanceLevel } from '@/shared/model/enumerations/importance-level.model';
import { type IPerson } from '@/shared/model/person.model';
import { type IPosition } from '@/shared/model/position.model';

export interface ICandidateProfile {
  id?: number;
  candidateDate?: Date;
  cultivateDirection?: string | null;
  stability?: keyof typeof ImportanceLevel | null;
  learningAbility?: keyof typeof ImportanceLevel | null;
  communicationCoordination?: keyof typeof ImportanceLevel | null;
  businessUnderstanding?: keyof typeof ImportanceLevel | null;
  responsibility?: keyof typeof ImportanceLevel | null;
  riskAwareness?: keyof typeof ImportanceLevel | null;
  judgement?: keyof typeof CandidateJudgement;
  evidence?: string | null;
  person?: IPerson;
  position?: IPosition | null;
  observer?: IPerson | null;
}

export class CandidateProfile implements ICandidateProfile {
  constructor(
    public id?: number,
    public candidateDate?: Date,
    public cultivateDirection?: string | null,
    public stability?: keyof typeof ImportanceLevel | null,
    public learningAbility?: keyof typeof ImportanceLevel | null,
    public communicationCoordination?: keyof typeof ImportanceLevel | null,
    public businessUnderstanding?: keyof typeof ImportanceLevel | null,
    public responsibility?: keyof typeof ImportanceLevel | null,
    public riskAwareness?: keyof typeof ImportanceLevel | null,
    public judgement?: keyof typeof CandidateJudgement,
    public evidence?: string | null,
    public person?: IPerson,
    public position?: IPosition | null,
    public observer?: IPerson | null,
  ) {}
}
