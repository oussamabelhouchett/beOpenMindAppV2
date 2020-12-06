import { NgModule } from '@angular/core';
import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';
import { AccueilPostComponent } from './accueil-post.component';
import { NewPostModalComponent } from './new-post-modal/new-post-modal.component';

@NgModule({
  imports: [BeOpenMindAppV2SharedModule],
  declarations: [AccueilPostComponent, NewPostModalComponent],
  exports: [AccueilPostComponent],
  entryComponents: [NewPostModalComponent],
})
export class AccueilPostModule {}
