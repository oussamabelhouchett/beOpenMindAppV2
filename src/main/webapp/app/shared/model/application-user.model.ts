export interface IApplicationUser {
  id?: number;
  additionalField?: number;
  userId?: number;
}

export class ApplicationUser implements IApplicationUser {
  constructor(public id?: number, public additionalField?: number, public userId?: number) {}
}
