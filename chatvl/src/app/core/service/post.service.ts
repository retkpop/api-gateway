import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from 'src/environments/environment';
import {CredentialsService} from '@app/core';
import {Videos} from '@app/core/models/Videos';

export interface DataContext {
  urlVideo: string;
  title: string;
  hashtag: [];
  type: number;
}
@Injectable({
  providedIn: 'root'
})
export class PostService {
  constructor(private credentialsService: CredentialsService, private http: HttpClient) {}
  checkVideo(urlVideo: string) {
    // tslint:disable-next-line:max-line-length
    return this.http.post<Videos>(`${environment.apiUrl}/api/posts/check-link/`, urlVideo , { withCredentials: true });
  }
  addVideo(data: DataContext) {
    console.log(data);
    data.type = 0;
    return this.http.post<Videos>(`${environment.apiUrl}/api/posts/add-video/`, data , { withCredentials: true });
  }
}

