import { NgModule } from '@angular/core';
import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';
import { AccueilPostComponent } from './accueil-post.component';

@NgModule({
  imports: [BeOpenMindAppV2SharedModule],
  declarations: [AccueilPostComponent],
  exports: [AccueilPostComponent],
})
export class AccueilPostModule {}
