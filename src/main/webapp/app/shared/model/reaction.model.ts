export interface IReaction {
  id?: number;
  isComment?: number;
  isLike?: number;
  postId?: number;
  userId?: number;
}

export class Reaction implements IReaction {
  constructor(public id?: number, public isComment?: number, public isLike?: number, public postId?: number, public userId?: number) {}
}
