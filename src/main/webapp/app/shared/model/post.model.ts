import { Moment } from 'moment';
import { IComments } from 'app/shared/model/comments.model';
import { IFilesPost } from 'app/shared/model/files-post.model';
import { IReaction } from './reaction.model';

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
  userId?: number;
  userAppId?: number;
  firstName?: string;
  lastName?: string;
  imageUrl?: string;
  currentUserReaction?: IReaction;
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
    public userId?: number,
    public userAppId?: number,
    public firstName?: string,
    public lastName?: string,
    public imageUrl?: string
  ) {
    this.isNameVisibale = this.isNameVisibale || false;
    this.isPhotoVisibale = this.isPhotoVisibale || false;
  }
}
