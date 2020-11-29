import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';

import { MetricsComponent } from './metrics.component';

import { metricsRoute } from './metrics.route';

@NgModule({
  imports: [BeOpenMindAppV2SharedModule, RouterModule.forChild([metricsRoute])],
  declarations: [MetricsComponent],
})
export class MetricsModule {}
