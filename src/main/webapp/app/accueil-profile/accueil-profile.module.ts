import { NgModule } from '@angular/core';
import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';
import { AccueilProfileComponent } from './accueil-profile.component';

@NgModule({
  imports: [BeOpenMindAppV2SharedModule],
  declarations: [AccueilProfileComponent],
  exports: [AccueilProfileComponent],
})
export class AccueilProfileModule {}
