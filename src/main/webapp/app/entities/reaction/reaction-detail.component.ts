import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReaction } from 'app/shared/model/reaction.model';

@Component({
  selector: 'jhi-reaction-detail',
  templateUrl: './reaction-detail.component.html',
})
export class ReactionDetailComponent implements OnInit {
  reaction: IReaction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reaction }) => (this.reaction = reaction));
  }

  previousState(): void {
    window.history.back();
  }
}
