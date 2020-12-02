import { Moment } from 'moment';
import { IComments } from 'app/shared/model/comments.model';
import { IFilesPost } from 'app/shared/model/files-post.model';
import { IApplicationUser } from 'app/shared/model/application-user.model';

export interface IPost {
  id?: number;
  title?: string;
  content?: string;
  datePub?: Moment;
  time?: Moment;
  isNameVisibale?: boolean;
  isPhotoVisibale?: boolean;
  nbreLike?: number;
  nbreComments?: number;
  comments?: IComments[];
  filesPosts?: IFilesPost[];
  users?: IApplicationUser[];
}

export class Post implements IPost {
  constructor(
    public id?: number,
    public title?: string,
    public content?: string,
    public datePub?: Moment,
    public time?: Moment,
    public isNameVisibale?: boolean,
    public isPhotoVisibale?: boolean,
    public nbreLike?: number,
    public nbreComments?: number,
    public comments?: IComments[],
    public filesPosts?: IFilesPost[],
    public users?: IApplicationUser[]
  ) {
    this.isNameVisibale = this.isNameVisibale || false;
    this.isPhotoVisibale = this.isPhotoVisibale || false;
  }
}
