import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GraphQLModule } from './graphql.module';
import { PostComponent } from './components/post/post.component';
import { Apollo } from 'apollo-angular';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    PostComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    GraphQLModule,
    ReactiveFormsModule,
  ],
  providers: [
    Apollo,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
