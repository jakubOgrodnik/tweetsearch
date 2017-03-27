package pl.kuba.search;

import java.util.List;

public interface SearchServiceInterface {
    List<LightTweet> search(String searchType, List<String> keywords);
}
