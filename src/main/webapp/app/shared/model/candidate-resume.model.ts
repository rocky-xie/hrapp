import { type IExternalCandidate } from '@/shared/model/external-candidate.model';

export interface ICandidateResume {
  id?: number;
  versionNumber?: number;
  receivedAt?: Date;
  originalFileName?: string;
  mediaType?: string | null;
  fileSize?: number;
  sha256?: string;
  storageKey?: string;
  structuredSummary?: string | null;
  activeVersion?: boolean;
  uploadedBy?: string;
  candidate?: IExternalCandidate | null;
}

export class CandidateResume implements ICandidateResume {
  constructor(
    public id?: number,
    public versionNumber?: number,
    public receivedAt?: Date,
    public originalFileName?: string,
    public mediaType?: string | null,
    public fileSize?: number,
    public sha256?: string,
    public storageKey?: string,
    public structuredSummary?: string | null,
    public activeVersion?: boolean,
    public uploadedBy?: string,
    public candidate?: IExternalCandidate | null,
  ) {
    this.activeVersion = this.activeVersion ?? false;
  }
}
