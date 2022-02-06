import { variable } from '@angular/compiler/src/output/output_ast';
import { Injectable } from '@angular/core';
import { Apollo, gql } from 'apollo-angular';
import { map } from 'rxjs/operators';
import { AddPost, Post } from 'src/app/models/post';
import { ADD_POST } from './post.mutation';
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
      query: POSTS_ALL,
      fetchPolicy: "network-only"
    }).valueChanges.pipe(map(({data}) => {
      console.log(data);
      return data.posts
    }));
  }

  addPost(addPost: AddPost) {
    const {title, content} = addPost;
    return this.apollo.mutate({
      mutation: ADD_POST,
      variables: {
        title,
        content
      }
    })
  }
}
