import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { find, map, Observable } from 'rxjs';
import { Post } from 'src/app/models/post';
import { PostService } from 'src/app/services/post/post.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {

  posts!: Observable<Post[]>;
  postForm!: UntypedFormGroup;

  constructor(
    private postService: PostService
  ) { }

  ngOnInit(): void {
    this.postForm = new UntypedFormGroup({
      title: new UntypedFormControl(''),
      content: new UntypedFormControl(''),
    })
    this.posts = this.postService.getAllPosts();
  }

  test(id: number): Observable<Post> {
    return this.posts.pipe(
      map(v => v.find(v => v.id === id) as Post)
    )
  }

  onAdd(): void {
    this.postService.addPost(this.postForm.value).subscribe(() => {
      this.posts = this.postService.getAllPosts();
    })
  }

  onDelete(id: number): void {
    this.postService.deletePost(id).subscribe(() => {
      this.posts = this.postService.getAllPosts();
    })
  }

}
