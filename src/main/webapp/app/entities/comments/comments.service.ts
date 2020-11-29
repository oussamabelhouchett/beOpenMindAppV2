import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IComments } from 'app/shared/model/comments.model';

type EntityResponseType = HttpResponse<IComments>;
type EntityArrayResponseType = HttpResponse<IComments[]>;

@Injectable({ providedIn: 'root' })
export class CommentsService {
  public resourceUrl = SERVER_API_URL + 'api/comments';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/comments';

  constructor(protected http: HttpClient) {}

  create(comments: IComments): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comments);
    return this.http
      .post<IComments>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(comments: IComments): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comments);
    return this.http
      .put<IComments>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IComments>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IComments[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IComments[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(comments: IComments): IComments {
    const copy: IComments = Object.assign({}, comments, {
      datePub: comments.datePub && comments.datePub.isValid() ? comments.datePub.format(DATE_FORMAT) : undefined,
      time: comments.time && comments.time.isValid() ? comments.time.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datePub = res.body.datePub ? moment(res.body.datePub) : undefined;
      res.body.time = res.body.time ? moment(res.body.time) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((comments: IComments) => {
        comments.datePub = comments.datePub ? moment(comments.datePub) : undefined;
        comments.time = comments.time ? moment(comments.time) : undefined;
      });
    }
    return res;
  }
}
