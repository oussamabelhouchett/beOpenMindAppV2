import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';

import { AuditsComponent } from './audits.component';

import { auditsRoute } from './audits.route';

@NgModule({
  imports: [BeOpenMindAppV2SharedModule, RouterModule.forChild([auditsRoute])],
  declarations: [AuditsComponent],
})
export class AuditsModule {}
