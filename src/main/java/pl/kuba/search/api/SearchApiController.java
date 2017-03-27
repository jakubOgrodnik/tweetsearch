package pl.kuba.search.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.kuba.search.LightTweet;
import pl.kuba.search.SearchServiceInterface;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchApiController {
    private SearchServiceInterface searchServiceInterface;
    @Autowired
    public SearchApiController(SearchServiceInterface searchServiceInterface) {
        this.searchServiceInterface = searchServiceInterface;
    }
    @RequestMapping(value = "/{searchType}", method = RequestMethod.GET)
    public List<LightTweet> search(@PathVariable String searchType,
                                   @MatrixVariable List<String> keywords) {
        return searchServiceInterface.search(searchType, keywords);
    }
}
