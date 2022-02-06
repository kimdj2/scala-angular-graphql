import { Injectable } from '@angular/core';
import { Apollo, gql } from 'apollo-angular';
import { map } from 'rxjs/operators';
import { Post } from 'src/app/models/post';
import { POSTS_ALL } from './post.query';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(
    private apollo: Apollo
  ) {}
  
  /**
   * Postリストを取得
   * @returns posts
   */
  getAllPosts() {
    return this.apollo.watchQuery<{posts: Post[]}>({
      query: POSTS_ALL
    }).valueChanges.pipe(map(({data}) => data.posts));
  }
}
