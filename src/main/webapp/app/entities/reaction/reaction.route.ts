import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IReaction, Reaction } from 'app/shared/model/reaction.model';
import { ReactionService } from './reaction.service';
import { ReactionComponent } from './reaction.component';
import { ReactionDetailComponent } from './reaction-detail.component';
import { ReactionUpdateComponent } from './reaction-update.component';

@Injectable({ providedIn: 'root' })
export class ReactionResolve implements Resolve<IReaction> {
  constructor(private service: ReactionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReaction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((reaction: HttpResponse<Reaction>) => {
          if (reaction.body) {
            return of(reaction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Reaction());
  }
}

export const reactionRoute: Routes = [
  {
    path: '',
    component: ReactionComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Reactions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReactionDetailComponent,
    resolve: {
      reaction: ReactionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Reactions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReactionUpdateComponent,
    resolve: {
      reaction: ReactionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Reactions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReactionUpdateComponent,
    resolve: {
      reaction: ReactionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Reactions',
    },
    canActivate: [UserRouteAccessService],
  },
];
