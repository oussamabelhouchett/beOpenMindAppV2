import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFilesPost, FilesPost } from 'app/shared/model/files-post.model';
import { FilesPostService } from './files-post.service';
import { FilesPostComponent } from './files-post.component';
import { FilesPostDetailComponent } from './files-post-detail.component';
import { FilesPostUpdateComponent } from './files-post-update.component';

@Injectable({ providedIn: 'root' })
export class FilesPostResolve implements Resolve<IFilesPost> {
  constructor(private service: FilesPostService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFilesPost> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((filesPost: HttpResponse<FilesPost>) => {
          if (filesPost.body) {
            return of(filesPost.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FilesPost());
  }
}

export const filesPostRoute: Routes = [
  {
    path: '',
    component: FilesPostComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'FilesPosts',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FilesPostDetailComponent,
    resolve: {
      filesPost: FilesPostResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FilesPosts',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FilesPostUpdateComponent,
    resolve: {
      filesPost: FilesPostResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FilesPosts',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FilesPostUpdateComponent,
    resolve: {
      filesPost: FilesPostResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FilesPosts',
    },
    canActivate: [UserRouteAccessService],
  },
];
