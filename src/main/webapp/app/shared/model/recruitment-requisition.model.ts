import { type RecruitmentType } from '@/shared/model/enumerations/recruitment-type.model';
import { type RequisitionEditStatus } from '@/shared/model/enumerations/requisition-edit-status.model';
import { type RequisitionLifecycle } from '@/shared/model/enumerations/requisition-lifecycle.model';
export interface IRecruitmentRequisition {
  id?: number;
  requisitionCode?: string;
  title?: string;
  department?: string;
  positionId?: number | null;
  recruitmentType?: keyof typeof RecruitmentType;
  lifecycle?: keyof typeof RequisitionLifecycle;
  editStatus?: keyof typeof RequisitionEditStatus;
  plannedHeadcount?: number;
  hiredCount?: number;
  checkedInCount?: number;
  priority?: string | null;
  reason?: string | null;
  targetJoinDate?: Date | null;
  desiredStartDate?: Date | null;
  desiredEndDate?: Date | null;
  currentVersion?: number;
  lockedAt?: Date | null;
  lockedBy?: string | null;
  unlockedAt?: Date | null;
  unlockReason?: string | null;
  requirementsJson?: string | null;
}

export class RecruitmentRequisition implements IRecruitmentRequisition {
  constructor(
    public id?: number,
    public requisitionCode?: string,
    public title?: string,
    public department?: string,
    public positionId?: number | null,
    public recruitmentType?: keyof typeof RecruitmentType,
    public lifecycle?: keyof typeof RequisitionLifecycle,
    public editStatus?: keyof typeof RequisitionEditStatus,
    public plannedHeadcount?: number,
    public hiredCount?: number,
    public checkedInCount?: number,
    public priority?: string | null,
    public reason?: string | null,
    public targetJoinDate?: Date | null,
    public desiredStartDate?: Date | null,
    public desiredEndDate?: Date | null,
    public currentVersion?: number,
    public lockedAt?: Date | null,
    public lockedBy?: string | null,
    public unlockedAt?: Date | null,
    public unlockReason?: string | null,
    public requirementsJson?: string | null,
  ) {}
}
