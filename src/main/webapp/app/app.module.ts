import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { BeOpenMindAppV2SharedModule } from 'app/shared/shared.module';
import { BeOpenMindAppV2CoreModule } from 'app/core/core.module';
import { BeOpenMindAppV2AppRoutingModule } from './app-routing.module';
import { BeOpenMindAppV2HomeModule } from './home/home.module';
import { BeOpenMindAppV2EntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    BeOpenMindAppV2SharedModule,
    BeOpenMindAppV2CoreModule,
    BeOpenMindAppV2HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    BeOpenMindAppV2EntityModule,
    BeOpenMindAppV2AppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class BeOpenMindAppV2AppModule {}
