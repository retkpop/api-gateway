import { NgModule } from '@angular/core';
import { Routes, RouterModule, PreloadAllModules } from '@angular/router';
import {ShellComponent} from '@app/shell/shell.component';
import {AboutComponent} from '@app/about/about.component';
import {extract} from '@app/core';
import {AddComponent} from '@app/add/add.component';

const routes: Routes = [
  {
    path: '',
    component: ShellComponent,
    children: [
      { path: 'about', component: AboutComponent, data: { title: extract('About') } },
      { path: 'add', component: AddComponent, data: { title: extract('Add') } }
    ],
    data: { reuse: true }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule {}
