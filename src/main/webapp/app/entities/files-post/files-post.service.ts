import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IFilesPost } from 'app/shared/model/files-post.model';

type EntityResponseType = HttpResponse<IFilesPost>;
type EntityArrayResponseType = HttpResponse<IFilesPost[]>;

@Injectable({ providedIn: 'root' })
export class FilesPostService {
  public resourceUrl = SERVER_API_URL + 'api/files-posts';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/files-posts';

  constructor(protected http: HttpClient) {}

  create(filesPost: IFilesPost): Observable<EntityResponseType> {
    return this.http.post<IFilesPost>(this.resourceUrl, filesPost, { observe: 'response' });
  }

  update(filesPost: IFilesPost): Observable<EntityResponseType> {
    return this.http.put<IFilesPost>(this.resourceUrl, filesPost, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFilesPost>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFilesPost[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFilesPost[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
