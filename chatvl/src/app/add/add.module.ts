import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { AddRoutingModule } from './add-routing.module';
import { AddComponent } from './add.component';

import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import {NgxInputTagModule} from '@ngx-lite/input-tag';
import {ModalModule} from 'ngx-bootstrap';
import {NgxYoutubePlayerModule} from 'ngx-youtube-player';


@NgModule({
  imports: [CommonModule, TranslateModule, AddRoutingModule, BrowserModule, ReactiveFormsModule, NgxInputTagModule, ModalModule, NgxYoutubePlayerModule],
  declarations: [AddComponent]
})
export class AddModule {}
