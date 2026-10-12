import { type ActionPriority } from '@/shared/model/enumerations/action-priority.model';
import { type ActionSourceType } from '@/shared/model/enumerations/action-source-type.model';
import { type ActionStatus } from '@/shared/model/enumerations/action-status.model';
export interface IActionItem {
  id?: number;
  sourceType?: keyof typeof ActionSourceType | null;
  sourceId?: number | null;
  sourceEntityType?: string | null;
  description?: string;
  assignee?: string | null;
  dueDate?: Date | null;
  status?: keyof typeof ActionStatus;
  priority?: keyof typeof ActionPriority | null;
  createdAt?: Date | null;
  completedAt?: Date | null;
  note?: string | null;
}

export class ActionItem implements IActionItem {
  constructor(
    public id?: number,
    public sourceType?: keyof typeof ActionSourceType | null,
    public sourceId?: number | null,
    public sourceEntityType?: string | null,
    public description?: string,
    public assignee?: string | null,
    public dueDate?: Date | null,
    public status?: keyof typeof ActionStatus,
    public priority?: keyof typeof ActionPriority | null,
    public createdAt?: Date | null,
    public completedAt?: Date | null,
    public note?: string | null,
  ) {}
}
