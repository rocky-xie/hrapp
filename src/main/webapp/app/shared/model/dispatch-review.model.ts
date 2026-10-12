import { type IDispatchAssignment } from '@/shared/model/dispatch-assignment.model';

export interface IDispatchReview {
  id?: number;
  reviewedAt?: Date;
  reviewerUsername?: string;
  result?: string | null;
  recommendation?: string | null;
  extensionEndDate?: Date | null;
  finalDecision?: string | null;
  dispatchAssignment?: IDispatchAssignment | null;
}

export class DispatchReview implements IDispatchReview {
  constructor(
    public id?: number,
    public reviewedAt?: Date,
    public reviewerUsername?: string,
    public result?: string | null,
    public recommendation?: string | null,
    public extensionEndDate?: Date | null,
    public finalDecision?: string | null,
    public dispatchAssignment?: IDispatchAssignment | null,
  ) {}
}
