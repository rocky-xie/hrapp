import { type RecruitmentType } from '@/shared/model/enumerations/recruitment-type.model';
export interface INotificationTemplate {
  id?: number;
  eventType?: string;
  recruitmentType?: keyof typeof RecruitmentType | null;
  language?: string;
  versionNumber?: number;
  subjectTemplate?: string;
  bodyTemplate?: string;
  externalSafe?: boolean;
  published?: boolean;
}

export class NotificationTemplate implements INotificationTemplate {
  constructor(
    public id?: number,
    public eventType?: string,
    public recruitmentType?: keyof typeof RecruitmentType | null,
    public language?: string,
    public versionNumber?: number,
    public subjectTemplate?: string,
    public bodyTemplate?: string,
    public externalSafe?: boolean,
    public published?: boolean,
  ) {
    this.externalSafe = this.externalSafe ?? false;
    this.published = this.published ?? false;
  }
}
