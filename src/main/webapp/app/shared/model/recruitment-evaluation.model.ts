import { type ApplicationStageType } from '@/shared/model/enumerations/application-stage-type.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

export interface IRecruitmentEvaluation {
  id?: number;
  stageType?: keyof typeof ApplicationStageType;
  activityId?: number | null;
  templateVersionId?: number | null;
  evaluatorUsername?: string;
  recordedByUsername?: string;
  status?: string;
  overallRecommendation?: string | null;
  totalScore?: number | null;
  summary?: string | null;
  answersJson?: string | null;
  evaluatedAt?: Date | null;
  submittedAt?: Date | null;
  publishedAt?: Date | null;
  correctionOfId?: number | null;
  application?: IRecruitmentApplication | null;
}

export class RecruitmentEvaluation implements IRecruitmentEvaluation {
  constructor(
    public id?: number,
    public stageType?: keyof typeof ApplicationStageType,
    public activityId?: number | null,
    public templateVersionId?: number | null,
    public evaluatorUsername?: string,
    public recordedByUsername?: string,
    public status?: string,
    public overallRecommendation?: string | null,
    public totalScore?: number | null,
    public summary?: string | null,
    public answersJson?: string | null,
    public evaluatedAt?: Date | null,
    public submittedAt?: Date | null,
    public publishedAt?: Date | null,
    public correctionOfId?: number | null,
    public application?: IRecruitmentApplication | null,
  ) {}
}
