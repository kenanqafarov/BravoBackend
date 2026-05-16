package com.bravo.rewardsai.controller;

import com.bravo.rewardsai.entity.Product;
import com.bravo.rewardsai.repository.ProductRepository;
import com.bravo.rewardsai.util.ApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    private ProductRepository productRepository;

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${gemini.model:gemini-2.0-flash}")
    private String geminiModel;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestClient restClient = RestClient.create();

    private final List<MockProduct> catalog = List.of(
            new MockProduct("p01", "Bravo Toyuq filesi", "Bravo Fresh", "protein", "500 q", 6.50, 550, 115, 0, 12,
                    "https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=300", List.of("protein", "fitness", "low-carb")),
            new MockProduct("p02", "Yunan yoqurtu", "Pinar", "protein", "400 q", 2.30, 150, 15, 8, 5,
                    "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=300", List.of("breakfast", "protein", "light")),
            new MockProduct("p03", "Avokado", "Fresh", "fat", "1 eded", 3.50, 160, 2, 9, 15,
                    "https://images.unsplash.com/photo-1523049673857-eb18f1d7b578?w=300", List.of("healthy-fat", "keto")),
            new MockProduct("p04", "Yulaf ezmesi", "Myllyn Paras", "carb", "1 kq", 1.80, 380, 13, 67, 7,
                    "https://images.unsplash.com/photo-1586444248902-2f64eddf13cf?w=300", List.of("breakfast", "budget", "fiber")),
            new MockProduct("p05", "Yumurta", "Bravo", "protein", "10 eded", 1.95, 700, 60, 4, 50,
                    "https://images.unsplash.com/photo-1506976785307-8732e854ad03?w=300", List.of("budget", "protein", "breakfast")),
            new MockProduct("p06", "Qulancar", "Fresh", "vegetable", "250 q", 7.90, 20, 2, 4, 0,
                    "https://images.unsplash.com/photo-1515471209610-dae1c92d8777?w=300", List.of("vegetable", "low-calorie")),
            new MockProduct("p07", "Qizil baliq filesi", "Sea Fresh", "protein", "200 q", 14.50, 400, 40, 0, 26,
                    "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=300", List.of("omega-3", "protein")),
            new MockProduct("p08", "Banan", "Fresh", "fruit", "1 kq", 2.80, 890, 11, 230, 3,
                    "https://images.unsplash.com/photo-1571771894821-ad996211fdf4?w=300", List.of("fruit", "energy")),
            new MockProduct("p09", "Tam bugda coreyi", "Baker House", "bakery", "450 q", 2.10, 980, 32, 180, 12,
                    "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=300", List.of("fiber", "breakfast")),
            new MockProduct("p10", "Tuna konservi", "Dardanel", "protein", "160 q", 3.20, 190, 38, 0, 3,
                    "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=300", List.of("protein", "quick")),
            new MockProduct("p11", "Qarisiq salat", "Fresh", "vegetable", "300 q", 2.70, 55, 4, 10, 1,
                    "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=300", List.of("salad", "low-calorie")),
            new MockProduct("p12", "Badam", "Nutline", "snack", "150 q", 5.40, 870, 32, 32, 75,
                    "https://images.unsplash.com/photo-1508061253366-f7da158b6d46?w=300", List.of("snack", "healthy-fat")),
            new MockProduct("p13", "Qrecka", "Bravo", "carb", "800 q", 2.60, 2740, 104, 560, 28,
                    "https://images.unsplash.com/photo-1604335399105-a0c585fd81a1?w=300", List.of("budget", "meal-prep")),
            new MockProduct("p14", "Kefir", "Atena", "dairy", "1 l", 2.20, 420, 32, 44, 18,
                    "https://images.unsplash.com/photo-1550583724-125581f77833?w=300", List.of("dairy", "breakfast")),
            new MockProduct("p15", "Alma", "Fresh", "fruit", "1 kq", 2.40, 520, 3, 138, 2,
                    "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=300", List.of("fruit", "fiber"))
    );

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<Map<String, String>>> chat(@RequestBody Map<String, String> payload) {
        BasketChatResponse basketResponse = buildBasketResponse(new BasketChatRequest(payload.getOrDefault("message", ""), List.of()));
        Map<String, String> response = new HashMap<>();
        response.put("reply", basketResponse.reply());
        return ResponseEntity.ok(ApiResponse.success(response, "AI response fetched"));
    }

    @PostMapping("/basket-chat")
    public ResponseEntity<ApiResponse<BasketChatResponse>> basketChat(@RequestBody BasketChatRequest request) {
        return ResponseEntity.ok(ApiResponse.success(buildBasketResponse(request), "AI basket generated"));
    }

    @GetMapping("/catalog")
    public ResponseEntity<ApiResponse<List<MockProduct>>> catalog() {
        return ResponseEntity.ok(ApiResponse.success(catalog, "AI product catalog fetched"));
    }

    @GetMapping("/generate-basket")
    public ResponseEntity<ApiResponse<List<Product>>> generateBasket() {
        List<Product> basketItems = productRepository.findByIsRecommendedTrue();
        return ResponseEntity.ok(ApiResponse.success(basketItems, "Healthy basket generated successfully!"));
    }

    private BasketChatResponse buildBasketResponse(BasketChatRequest request) {
        String prompt = buildPrompt(request);
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            return fallbackBasket(request.message(), "Gemini API key is not configured, fallback basket hazirlandi.");
        }

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/" + geminiModel + ":generateContent?key=" + geminiApiKey;
            Map<String, Object> body = Map.of(
                    "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))),
                    "generationConfig", Map.of(
                            "temperature", 0.35,
                            "responseMimeType", "application/json"
                    )
            );

            String rawResponse = restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            String text = objectMapper.readTree(rawResponse)
                    .path("candidates").path(0)
                    .path("content").path("parts").path(0)
                    .path("text").asText();

            return parseBasketResponse(text);
        } catch (Exception ex) {
            return fallbackBasket(request.message(), "Gemini cavabi alina bilmedi, ehtiyat sebet hazirlandi.");
        }
    }

    private String buildPrompt(BasketChatRequest request) {
        Map<String, Object> promptData = new LinkedHashMap<>();
        promptData.put("userMessage", request.message());
        promptData.put("currentBasket", request.currentBasket());
        promptData.put("catalog", catalog);

        try {
            return """
                    You are Bravo AI, a smart supermarket basket assistant for Azerbaijan.
                    The user can ask for budget, calories, protein, diet preferences, or changes to the current basket.
                    Use ONLY products from the catalog.
                    If the user asks to change the basket, modify the current basket instead of starting from zero.
                    Keep total price close to requested budget and calories close to requested target when possible.
                    Return strict JSON only. No markdown.
                    Schema:
                    {
                      "reply": "short Azerbaijani explanation",
                      "items": [
                        {"productId":"p01","quantity":1,"reason":"why selected"}
                      ],
                      "totals": {"price": 0, "calories": 0, "protein": 0, "carbs": 0, "fat": 0},
                      "needsApproval": true
                    }
                    Input:
                    """ + objectMapper.writeValueAsString(promptData);
        } catch (Exception ex) {
            return request.message();
        }
    }

    private BasketChatResponse parseBasketResponse(String text) throws Exception {
        String cleaned = text.replaceAll("^```json\\s*", "").replaceAll("^```\\s*", "").replaceAll("\\s*```$", "").trim();
        JsonNode root = objectMapper.readTree(cleaned);
        List<BasketItem> items = new ArrayList<>();

        for (JsonNode itemNode : root.path("items")) {
            String productId = itemNode.path("productId").asText();
            int quantity = Math.max(1, itemNode.path("quantity").asInt(1));
            String reason = itemNode.path("reason").asText("");
            findProduct(productId).ifPresent(product -> items.add(toBasketItem(product, quantity, reason)));
        }

        Totals totals = calculateTotals(items);
        return new BasketChatResponse(
                root.path("reply").asText("Sebet hazirdir. Tesdiq ede ve ya deyisiklik isteye bilersiniz."),
                items,
                totals,
                root.path("needsApproval").asBoolean(true)
        );
    }

    private BasketChatResponse fallbackBasket(String message, String replyPrefix) {
        String normalized = message == null ? "" : message.toLowerCase(Locale.ROOT);
        double budget = extractBudget(normalized);
        int calorieTarget = extractCalories(normalized);

        List<MockProduct> candidates = new ArrayList<>();
        if (normalized.contains("protein") || normalized.contains("idman")) {
            candidates.addAll(List.of(catalog.get(0), catalog.get(1), catalog.get(9), catalog.get(10)));
        } else if (normalized.contains("seher") || normalized.contains("breakfast")) {
            candidates.addAll(List.of(catalog.get(3), catalog.get(4), catalog.get(1), catalog.get(14)));
        } else {
            candidates.addAll(List.of(catalog.get(0), catalog.get(3), catalog.get(10), catalog.get(14), catalog.get(1)));
        }

        List<BasketItem> items = new ArrayList<>();
        double total = 0;
        for (MockProduct product : candidates) {
            if (budget > 0 && total + product.price() > budget + 1.5) continue;
            items.add(toBasketItem(product, 1, "Budce ve qida balansina uygun secildi"));
            total += product.price();
            if (items.size() >= 5) break;
        }

        if (calorieTarget > 0 && calculateTotals(items).calories() < calorieTarget * 0.65) {
            findProduct("p08").ifPresent(product -> items.add(toBasketItem(product, 1, "Kalori hedefini tamamlamaq ucun")));
        }

        return new BasketChatResponse(replyPrefix + " Deyisiklik isteyirsinizse yazin, sebete duzelis edim.", items, calculateTotals(items), true);
    }

    private double extractBudget(String text) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d+(?:[.,]\\d+)?)\\s*(azn|manat)").matcher(text);
        if (matcher.find()) return Double.parseDouble(matcher.group(1).replace(",", "."));
        return 20.0;
    }

    private int extractCalories(String text) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d{3,4})\\s*(kcal|kalori)").matcher(text);
        if (matcher.find()) return Integer.parseInt(matcher.group(1));
        return 0;
    }

    private java.util.Optional<MockProduct> findProduct(String productId) {
        return catalog.stream().filter(product -> product.id().equals(productId)).findFirst();
    }

    private BasketItem toBasketItem(MockProduct product, int quantity, String reason) {
        return new BasketItem(
                product.id(),
                product.name(),
                product.brand(),
                product.category(),
                product.packageSize(),
                product.price(),
                product.calories(),
                product.protein(),
                product.carbs(),
                product.fat(),
                product.imageUrl(),
                product.tags(),
                quantity,
                round(product.price() * quantity),
                product.calories() * quantity,
                product.protein() * quantity,
                product.carbs() * quantity,
                product.fat() * quantity,
                reason
        );
    }

    private Totals calculateTotals(List<BasketItem> items) {
        double price = 0;
        int calories = 0;
        double protein = 0;
        double carbs = 0;
        double fat = 0;
        for (BasketItem item : items) {
            price += item.lineTotal();
            calories += item.totalCalories();
            protein += item.totalProtein();
            carbs += item.totalCarbs();
            fat += item.totalFat();
        }
        return new Totals(round(price), calories, round(protein), round(carbs), round(fat));
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public record BasketChatRequest(String message, List<BasketItem> currentBasket) {}

    public record BasketChatResponse(String reply, List<BasketItem> items, Totals totals, boolean needsApproval) {}

    public record Totals(double price, int calories, double protein, double carbs, double fat) {}

    public record MockProduct(
            String id,
            String name,
            String brand,
            String category,
            String packageSize,
            double price,
            int calories,
            double protein,
            double carbs,
            double fat,
            String imageUrl,
            List<String> tags
    ) {}

    public record BasketItem(
            String productId,
            String name,
            String brand,
            String category,
            String packageSize,
            double price,
            int calories,
            double protein,
            double carbs,
            double fat,
            String imageUrl,
            List<String> tags,
            int quantity,
            double lineTotal,
            int totalCalories,
            double totalProtein,
            double totalCarbs,
            double totalFat,
            String reason
    ) {}
}
