import { gql } from "apollo-angular";

export const ADD_POST = gql`
  mutation addPost($title: String!, $content: String!){
    post: addPost(title: $title, content: $content) {
      id
    }
  }
`;
