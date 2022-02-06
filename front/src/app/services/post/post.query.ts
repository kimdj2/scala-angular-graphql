import { gql } from "apollo-angular";

export const POSTS_ALL = gql`
  query {
    posts: postsAll {
      id
      title
      content
    }
  }
`;
