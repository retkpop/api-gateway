import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { extract } from '@app/core';
import { AboutComponent } from './about.component';
import {Shell} from '@app/shell/shell.service';

const routes: Routes = [
  // Module is lazy loaded, see app-routing.module.ts
  Shell.unAuthenticatedChildRoutes([
    { path: 'about', component: AboutComponent, data: { title: extract('About') } }
  ])
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class AboutRoutingModule {}
