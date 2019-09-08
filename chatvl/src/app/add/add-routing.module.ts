import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { extract } from '@app/core';
import { AddComponent } from './add.component';
import {Shell} from '@app/shell/shell.service';

const routes: Routes = [
  // Module is lazy loaded, see app-routing.module.ts
  Shell.childRoutes([
    { path: 'add', component: AddComponent, data: { title: extract('Add') } }
  ])
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class AddRoutingModule {}
