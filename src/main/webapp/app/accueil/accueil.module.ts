import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AccueilComponent } from './accueil.component';
import { ACCUEIL_ROUTE } from './accueil.route';
import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';
import { AccueilPostModule } from 'app/accueil-post/accueil-post.module';
import { AccueilProfileModule } from 'app/accueil-profile/accueil-profile.module';
import { AccueilghtSectionModule } from 'app/accueil-right-section/accueil-right-section.module';

@NgModule({
  imports: [
    BeOpenMindAppV2SharedModule,
    RouterModule.forChild([ACCUEIL_ROUTE]),
    AccueilPostModule,
    AccueilProfileModule,
    AccueilghtSectionModule,
  ],

  declarations: [AccueilComponent],
})
export class AccueilModule {}
