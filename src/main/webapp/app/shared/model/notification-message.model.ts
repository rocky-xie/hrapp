import { type NotificationMessageStatus } from '@/shared/model/enumerations/notification-message-status.model';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

export interface INotificationMessage {
  id?: number;
  eventType?: string;
  templateId?: number | null;
  status?: keyof typeof NotificationMessageStatus;
  sendMode?: string;
  finalSubject?: string;
  finalBody?: string;
  recipientsJson?: string;
  retryCount?: number;
  maxRetries?: number;
  lastError?: string | null;
  createdBy?: string;
  confirmedBy?: string | null;
  createdAt?: Date;
  confirmedAt?: Date | null;
  sentAt?: Date | null;
  application?: IRecruitmentApplication | null;
}

export class NotificationMessage implements INotificationMessage {
  constructor(
    public id?: number,
    public eventType?: string,
    public templateId?: number | null,
    public status?: keyof typeof NotificationMessageStatus,
    public sendMode?: string,
    public finalSubject?: string,
    public finalBody?: string,
    public recipientsJson?: string,
    public retryCount?: number,
    public maxRetries?: number,
    public lastError?: string | null,
    public createdBy?: string,
    public confirmedBy?: string | null,
    public createdAt?: Date,
    public confirmedAt?: Date | null,
    public sentAt?: Date | null,
    public application?: IRecruitmentApplication | null,
  ) {}
}
