package com.MountBlue.Hacker.News.Clone.respository;

import com.MountBlue.Hacker.News.Clone.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor {
    List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrCreatedByContainingIgnoreCase(String search, String search1, String search2);

    List<Post> findAllByDomainName(String domainName);

    @Query(value = "select * from post where post.title ILike 'Show HN%';", nativeQuery = true)
    List<Post> findByContentStartingWithShow();

    @Query(value = "select * from post where post.title ILike 'Ask HN%';", nativeQuery = true)
    List<Post> findByContentStartingWithAsk();

    @Query(value = "select * from post where post.title ILike '%Hiring%';", nativeQuery = true)
    List<Post> findByContentContainingHiring();

    @Query(value = "select * from post where post.title NOT ILIKE '%Hiring%' and post.title NOT ILIKE 'Ask HN%' and post.title NOT ILIKE 'Show HN%';", nativeQuery = true)
    List<Post> findAllPostOfNews();
}
