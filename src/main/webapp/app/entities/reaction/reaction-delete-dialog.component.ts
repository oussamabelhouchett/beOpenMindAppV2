import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReaction } from 'app/shared/model/reaction.model';
import { ReactionService } from './reaction.service';

@Component({
  templateUrl: './reaction-delete-dialog.component.html',
})
export class ReactionDeleteDialogComponent {
  reaction?: IReaction;

  constructor(protected reactionService: ReactionService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reactionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('reactionListModification');
      this.activeModal.close();
    });
  }
}
