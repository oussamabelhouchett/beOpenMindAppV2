export interface IApplicationUser {
  id?: number;
  additionalField?: number;
  userId?: number;
  postId?: number;
  commentsId?: number;
  reactionId?: number;
}

export class ApplicationUser implements IApplicationUser {
  constructor(
    public id?: number,
    public additionalField?: number,
    public userId?: number,
    public postId?: number,
    public commentsId?: number,
    public reactionId?: number
  ) {}
}
