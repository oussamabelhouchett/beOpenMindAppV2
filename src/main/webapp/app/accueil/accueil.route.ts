import { Route } from '@angular/router';
import { AccueilComponent } from './accueil.component';

export const ACCUEIL_ROUTE: Route = {
  path: '',
  component: AccueilComponent,
  data: {
    authorities: [],
    pageTitle: 'Welcome, Java Hipster!',
  },
};
