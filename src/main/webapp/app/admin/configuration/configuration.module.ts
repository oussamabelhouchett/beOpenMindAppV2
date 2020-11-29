import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';

import { ConfigurationComponent } from './configuration.component';

import { configurationRoute } from './configuration.route';

@NgModule({
  imports: [BeOpenMindAppV2SharedModule, RouterModule.forChild([configurationRoute])],
  declarations: [ConfigurationComponent],
})
export class ConfigurationModule {}
