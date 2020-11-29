import { Moment } from 'moment';

export interface IComments {
  id?: number;
  contentText?: string;
  datePub?: Moment;
  time?: Moment;
  parentId?: number;
  commentsId?: number;
}

export class Comments implements IComments {
  constructor(
    public id?: number,
    public contentText?: string,
    public datePub?: Moment,
    public time?: Moment,
    public parentId?: number,
    public commentsId?: number
  ) {}
}
