import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';
import { FilesPostComponent } from './files-post.component';
import { FilesPostDetailComponent } from './files-post-detail.component';
import { FilesPostUpdateComponent } from './files-post-update.component';
import { FilesPostDeleteDialogComponent } from './files-post-delete-dialog.component';
import { filesPostRoute } from './files-post.route';

@NgModule({
  imports: [BeOpenMindAppV2SharedModule, RouterModule.forChild(filesPostRoute)],
  declarations: [FilesPostComponent, FilesPostDetailComponent, FilesPostUpdateComponent, FilesPostDeleteDialogComponent],
  entryComponents: [FilesPostDeleteDialogComponent],
})
export class BeOpenMindAppV2FilesPostModule {}
