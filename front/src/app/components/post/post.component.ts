import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { Post } from 'src/app/models/post';
import { PostService } from 'src/app/services/post/post.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {

  posts!: Observable<Post[]>;
  postForm!: FormGroup;

  constructor(
    private postService: PostService
  ) { }

  ngOnInit(): void {
    this.postForm = new FormGroup({
      title: new FormControl(''),
      content: new FormControl(''),
    })
    this.posts = this.postService.getAllPosts();
  }

  onAdd(): void{
    console.log(this.postForm.value)
    this.postService.addPost(this.postForm.value).subscribe(() => {
      this.posts = this.postService.getAllPosts();
    })
  }

}
