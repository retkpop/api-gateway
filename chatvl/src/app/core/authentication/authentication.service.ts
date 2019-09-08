import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, of} from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Credentials, CredentialsService } from './credentials.service';
import {map} from 'rxjs/operators';

export interface LoginContext {
  username: string;
  password: string;
  remember?: boolean;
}

/**
 * Provides a base for authentication workflow.
 * The login/logout methods should be replaced with proper implementation.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {




  constructor(private credentialsService: CredentialsService, private http: HttpClient) {}
  login(context: LoginContext): Observable<Credentials> {
    // Replace by proper authentication call
    const data = {
      username: context.username,
      password: context.password,
      remember: context.remember
    };
    // tslint:disable-next-line:max-line-length
    return this.http.post(`${environment.apiUrl}/api/authenticate`, data, { observe: 'response' }).pipe(map(authenticateSuccess.bind(this)));
    function authenticateSuccess(resp:any) {
      const bearerToken = resp.headers.get('Authorization');
      if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
        const jwt = bearerToken.slice(7, bearerToken.length);
        this.storeAuthenticationToken(jwt,data);
        return jwt;
      }
    }
  }
  storeAuthenticationToken(jwt:string, data:any) {
    const user = {
      username: data.username,
      token: jwt
    };
    this.credentialsService.setCredentials(user, data.remember);
  }

  /**
   * Logs out the user and clear credentials.
   * @return True if the user was logged out successfully.
   */
  logout(): Observable<boolean> {
    // Customize credentials invalidation here
    this.credentialsService.setCredentials();
    return of(true);
  }
}
