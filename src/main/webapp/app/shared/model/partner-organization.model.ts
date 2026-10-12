export interface IPartnerOrganization {
  id?: number;
  partnerCode?: string;
  name?: string;
  rolesJson?: string;
  dispatchEnabled?: boolean;
  defaultObservationDays?: number | null;
  active?: boolean;
}

export class PartnerOrganization implements IPartnerOrganization {
  constructor(
    public id?: number,
    public partnerCode?: string,
    public name?: string,
    public rolesJson?: string,
    public dispatchEnabled?: boolean,
    public defaultObservationDays?: number | null,
    public active?: boolean,
  ) {
    this.dispatchEnabled = this.dispatchEnabled ?? false;
    this.active = this.active ?? false;
  }
}
