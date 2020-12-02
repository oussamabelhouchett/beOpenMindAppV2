import { IApplicationUser } from 'app/shared/model/application-user.model';

export interface IReaction {
  id?: number;
  isComment?: number;
  isLike?: number;
  users?: IApplicationUser[];
  postId?: number;
}

export class Reaction implements IReaction {
  constructor(
    public id?: number,
    public isComment?: number,
    public isLike?: number,
    public users?: IApplicationUser[],
    public postId?: number
  ) {}
}
