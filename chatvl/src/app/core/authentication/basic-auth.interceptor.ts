import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

import {AuthenticationService, Credentials} from '@app/core';
const credentialsKey = 'credentials';

@Injectable()
export class BasicAuthInterceptor implements HttpInterceptor {

  private _credentials: Credentials | null = null;

    constructor(private authenticationService: AuthenticationService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // add authorization header with basic auth credentials if available
      const savedCredentials = sessionStorage.getItem(credentialsKey) || localStorage.getItem(credentialsKey);
      if (savedCredentials) {
        this._credentials = JSON.parse(savedCredentials);
            request = request.clone({
                setHeaders: { 
                    Authorization: `Bearer ${this._credentials.token}`
                }
            });
        }

        return next.handle(request);
    }
}
