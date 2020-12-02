import { Moment } from 'moment';

export interface IComments {
  id?: number;
  contentText?: string;
  datePub?: Moment;
  time?: Moment;
  parentId?: number;
  userId?: number;
  postId?: number;
}

export class Comments implements IComments {
  constructor(
    public id?: number,
    public contentText?: string,
    public datePub?: Moment,
    public time?: Moment,
    public parentId?: number,
    public userId?: number,
    public postId?: number
  ) {}
}
