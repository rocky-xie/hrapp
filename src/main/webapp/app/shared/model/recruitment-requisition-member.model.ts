import { type IRecruitmentRequisition } from '@/shared/model/recruitment-requisition.model';

export interface IRecruitmentRequisitionMember {
  id?: number;
  username?: string;
  memberType?: string;
  active?: boolean;
  createdAt?: Date;
  removedAt?: Date | null;
  requisition?: IRecruitmentRequisition | null;
}

export class RecruitmentRequisitionMember implements IRecruitmentRequisitionMember {
  constructor(
    public id?: number,
    public username?: string,
    public memberType?: string,
    public active?: boolean,
    public createdAt?: Date,
    public removedAt?: Date | null,
    public requisition?: IRecruitmentRequisition | null,
  ) {
    this.active = this.active ?? false;
  }
}
