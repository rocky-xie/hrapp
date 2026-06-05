import { type TrustStage } from '@/shared/model/enumerations/trust-stage.model';
import { type IPerson } from '@/shared/model/person.model';

export interface ITrustObservation {
  id?: number;
  observationDate?: Date;
  trustStage?: keyof typeof TrustStage;
  observedBehavior?: string | null;
  positiveSignal?: string | null;
  riskSignal?: string | null;
  nextObservationPoint?: string | null;
  person?: IPerson;
  observer?: IPerson | null;
}

export class TrustObservation implements ITrustObservation {
  constructor(
    public id?: number,
    public observationDate?: Date,
    public trustStage?: keyof typeof TrustStage,
    public observedBehavior?: string | null,
    public positiveSignal?: string | null,
    public riskSignal?: string | null,
    public nextObservationPoint?: string | null,
    public person?: IPerson,
    public observer?: IPerson | null,
  ) {}
}
