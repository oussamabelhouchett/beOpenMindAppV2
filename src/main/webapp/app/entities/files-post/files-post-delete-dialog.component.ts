import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFilesPost } from 'app/shared/model/files-post.model';
import { FilesPostService } from './files-post.service';

@Component({
  templateUrl: './files-post-delete-dialog.component.html',
})
export class FilesPostDeleteDialogComponent {
  filesPost?: IFilesPost;

  constructor(protected filesPostService: FilesPostService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.filesPostService.delete(id).subscribe(() => {
      this.eventManager.broadcast('filesPostListModification');
      this.activeModal.close();
    });
  }
}
