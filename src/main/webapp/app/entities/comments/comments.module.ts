import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';
import { CommentsComponent } from './comments.component';
import { CommentsDetailComponent } from './comments-detail.component';
import { CommentsUpdateComponent } from './comments-update.component';
import { CommentsDeleteDialogComponent } from './comments-delete-dialog.component';
import { commentsRoute } from './comments.route';

@NgModule({
  imports: [BeOpenMindAppV2SharedModule, RouterModule.forChild(commentsRoute)],
  declarations: [CommentsComponent, CommentsDetailComponent, CommentsUpdateComponent, CommentsDeleteDialogComponent],
  entryComponents: [CommentsDeleteDialogComponent],
})
export class BeOpenMindAppV2CommentsModule {}
