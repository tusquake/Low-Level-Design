interface SearchStrategy{
    void search(String query);
}

class LocationBasedSearchStrategy implements SearchStrategy{

    @Override
    public void search(String query) {
        System.out.println("Searching Based on Location" + query);
    }
}

class RatingBasedSearchStrategy implements SearchStrategy{

    @Override
    public void search(String query) {
        System.out.println("Searching Based on Rating" + query);
    }
}

class PriceOptimizedSearchStrategy implements SearchStrategy{

    @Override
    public void search(String query) {
        System.out.println("Searching Based on Rating" + query);
    }
}

class SearchStrategyFactory{
    public static SearchStrategy getStrategy(String userType, Boolean discountActive){
        if (discountActive) {
            return new PriceOptimizedSearchStrategy();
        }

        if ("PREMIUM".equalsIgnoreCase(userType)) {
            return new RatingBasedSearchStrategy();
        }

        return new LocationBasedSearchStrategy();
    }
}

class SearchOrchestrator {
    public void handleSearch(String query, String userType, Boolean discountActive) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }

        SearchStrategy strategy =
                SearchStrategyFactory.getStrategy(userType, discountActive);

        strategy.search(query);

        System.out.println("Search completed successfully");
    }
}

public class SearchSystem {

    public static void main(String[] args) {

        SearchOrchestrator orchestrator = new SearchOrchestrator();

        orchestrator.handleSearch(
                "AC Repair",
                "PREMIUM",
                false
        );
    }
}



