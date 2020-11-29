import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';

import { DocsComponent } from './docs.component';

import { docsRoute } from './docs.route';

@NgModule({
  imports: [BeOpenMindAppV2SharedModule, RouterModule.forChild([docsRoute])],
  declarations: [DocsComponent],
})
export class DocsModule {}
