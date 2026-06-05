export interface IKeyResponsibilityCategory {
  id?: number;
  categoryName?: string;
  examples?: string | null;
  riskFocus?: string | null;
}

export class KeyResponsibilityCategory implements IKeyResponsibilityCategory {
  constructor(
    public id?: number,
    public categoryName?: string,
    public examples?: string | null,
    public riskFocus?: string | null,
  ) {}
}
