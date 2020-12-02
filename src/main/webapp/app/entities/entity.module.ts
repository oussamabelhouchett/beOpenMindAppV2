import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'post',
        loadChildren: () => import('./post/post.module').then(m => m.BeOpenMindAppV2PostModule),
      },
      {
        path: 'files-post',
        loadChildren: () => import('./files-post/files-post.module').then(m => m.BeOpenMindAppV2FilesPostModule),
      },
      {
        path: 'comments',
        loadChildren: () => import('./comments/comments.module').then(m => m.BeOpenMindAppV2CommentsModule),
      },
      {
        path: 'reaction',
        loadChildren: () => import('./reaction/reaction.module').then(m => m.BeOpenMindAppV2ReactionModule),
      },
      {
        path: 'application-user',
        loadChildren: () => import('./application-user/application-user.module').then(m => m.BeOpenMindAppV2ApplicationUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class BeOpenMindAppV2EntityModule {}
