import { Routes, Route } from '@angular/router';

import { AuthenticationGuard } from '@app/core';
import { ShellComponent } from './shell.component';

/**
 * Provides helper methods to create routes.
 */
export class Shell {

  // existing helper
  static childRoutes(routes: Routes): Route {
    return {
      path: '',
      component: ShellComponent,
      children: routes,
      canActivate: [AuthenticationGuard],
      // Reuse ShellComponent instance when navigating between child views
      data: { reuse: true }
    };
  }

  // add new helper without guard
  static unAuthenticatedChildRoutes(routes: Routes): Route {
    return {
      path: '',
      component: ShellComponent,
      children: routes,
      // Reuse ShellComponent instance when navigating between child views
      data: { reuse: true }
    };
  }
}
