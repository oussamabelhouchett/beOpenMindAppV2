import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LoginComponent } from './login.component';
import { LOGIN_ROUTE } from './login.route';
import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [BeOpenMindAppV2SharedModule, RouterModule.forChild([LOGIN_ROUTE])],
  declarations: [LoginComponent],
})
export class LoginModule {}
