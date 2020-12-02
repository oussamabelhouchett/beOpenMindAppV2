import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IApplicationUser, ApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from './application-user.service';
import { ApplicationUserComponent } from './application-user.component';
import { ApplicationUserDetailComponent } from './application-user-detail.component';
import { ApplicationUserUpdateComponent } from './application-user-update.component';

@Injectable({ providedIn: 'root' })
export class ApplicationUserResolve implements Resolve<IApplicationUser> {
  constructor(private service: ApplicationUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApplicationUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((applicationUser: HttpResponse<ApplicationUser>) => {
          if (applicationUser.body) {
            return of(applicationUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApplicationUser());
  }
}

export const applicationUserRoute: Routes = [
  {
    path: '',
    component: ApplicationUserComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ApplicationUsers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApplicationUserDetailComponent,
    resolve: {
      applicationUser: ApplicationUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ApplicationUsers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApplicationUserUpdateComponent,
    resolve: {
      applicationUser: ApplicationUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ApplicationUsers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApplicationUserUpdateComponent,
    resolve: {
      applicationUser: ApplicationUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ApplicationUsers',
    },
    canActivate: [UserRouteAccessService],
  },
];
