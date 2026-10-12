import { type IRecruitmentRequisition } from '@/shared/model/recruitment-requisition.model';

export interface IRecruitmentRequisitionVersion {
  id?: number;
  versionNumber?: number;
  snapshotJson?: string;
  lockedAt?: Date;
  lockedBy?: string;
  changeReason?: string | null;
  requisition?: IRecruitmentRequisition | null;
}

export class RecruitmentRequisitionVersion implements IRecruitmentRequisitionVersion {
  constructor(
    public id?: number,
    public versionNumber?: number,
    public snapshotJson?: string,
    public lockedAt?: Date,
    public lockedBy?: string,
    public changeReason?: string | null,
    public requisition?: IRecruitmentRequisition | null,
  ) {}
}
