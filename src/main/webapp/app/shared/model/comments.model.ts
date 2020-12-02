import { Moment } from 'moment';
import { IApplicationUser } from 'app/shared/model/application-user.model';

export interface IComments {
  id?: number;
  contentText?: string;
  datePub?: Moment;
  time?: Moment;
  users?: IApplicationUser[];
  parentId?: number;
  postId?: number;
}

export class Comments implements IComments {
  constructor(
    public id?: number,
    public contentText?: string,
    public datePub?: Moment,
    public time?: Moment,
    public users?: IApplicationUser[],
    public parentId?: number,
    public postId?: number
  ) {}
}
