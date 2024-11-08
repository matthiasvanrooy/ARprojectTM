package fact.it.productservice.service;

import fact.it.productservice.dto.ProductResponse;
import fact.it.productservice.model.Product;
import fact.it.productservice.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static fact.it.productservice.model.Category.FRUIT;
import static fact.it.productservice.model.Category.VEGETABLE;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @PostConstruct
    public void loadData() {
        if(productRepository.count() <= 0){
            Product product1 = new Product();
            product1.setSkuCode("CAR001");
            product1.setName("Carrot");
            product1.setDescription("Wortels eten is supergoed en leuk, en ik zal je vertellen waarom!\n" +
                    "Ten eerste, wortels zijn eigenlijk een soort superhelden voor je ogen. Als je wortels eet, krijg je namelijk supergezonde ogen, zodat je altijd goed kunt zien – zelfs in het donker! Misschien kun je dan straks net zo goed zien als een kat in de nacht.\n" +
                    "En wist je dat wortels je laten stralen? Je huid wordt er mooier van, en je krijgt een gezonde glans, alsof je net lekker buiten hebt gespeeld.\n" +
                    "Wortels zijn ook goed voor je tanden. Als je erop kauwt, worden je tanden sterk en schoon, net als die van een konijntje.\n" +
                    "En oh ja, wortels zijn heel vrolijk van kleur. Dat feloranje maakt alles een beetje feestelijker, bijna zoals een regenboog op je bord. Dus als je wortels eet, is het elke keer een klein feestje voor je buik!\n" +
                    "Dus, neem een hap van die knapperige, knaloranje wortel en word een wortel-superheld!");
            product1.setCategory(VEGETABLE);
            product1.setPrice(BigDecimal.valueOf(2));

            Product product2 = new Product();
            product2.setSkuCode("PEP001");
            product2.setName("Pepper");
            product2.setDescription("Peper helpt je ook om beter te ademen en kan zelfs je neus een beetje openmaken als je verkouden bent. \n" +
                    "Ze zitten vol vitamines, vooral vitamine C, die je helpt om gezond en sterk te blijven.\n" +
                    "Peper zorgt ook voor een beetje pit in je eten, wat alles lekkerder en leuker kan maken.\n" +
                    "Dus als je durft, probeer eens een klein hapje peper en voel de energie die het je geeft – een echt krachtvoer!");
            product2.setCategory(VEGETABLE);
            product2.setPrice(BigDecimal.valueOf(5));

            Product product3 = new Product();
            product3.setSkuCode("CUC001");
            product3.setName("Cucumber");
            product3.setDescription("Komkommers zijn lekker en gezond, en ik zal je vertellen waarom je er eentje zou moeten eten!\n" +
                    "Ze zitten vol water, dus ze helpen je om lekker fris te blijven na het spelen. Komkommers zorgen ook voor een zachte, frisse huid, alsof je net uit het zwembad komt. Ze helpen je om goed te groeien en maken je sterk.\n" +
                    "Bovendien zijn ze heerlijk knapperig en smaken ze fris, net als een hapje van de zomer. Dus neem een stukje komkommer en geniet van een frisse, gezonde snack!");
            product3.setCategory(VEGETABLE);
            product3.setPrice(BigDecimal.valueOf(2));

            Product product4 = new Product();
            product4.setSkuCode("APP001");
            product4.setName("Granny Smith Apple");
            product4.setDescription("Appels zijn heerlijk en geven je een flinke energieboost! \n" +
                    "Ze zitten vol vezels en vitamines die je sterk maken, zodat je volop kunt spelen en leren. \n" +
                    "Appels helpen ook om je tanden schoon te houden wanneer je erop kauwt. \n" +
                    "En met hun frisse, zoete smaak is elke hap een klein feestje. Een appel per dag, en je voelt je kiplekker!");
            product4.setCategory(FRUIT);
            product4.setPrice(BigDecimal.valueOf(2));

            Product product5 = new Product();
            product5.setSkuCode("PEA001");
            product5.setName("Conference Pear");
            product5.setDescription("Peren zijn zacht en sappig, perfect om van te smullen! \n" +
                    "Ze zitten vol gezonde voedingsstoffen die je lichaam helpen groeien en je een fijne energie geven.\n" +
                    "Met hun zoete smaak en zachte bite is een peer een fijne, gezonde traktatie!");
            product5.setCategory(FRUIT);
            product5.setPrice(BigDecimal.valueOf(3));

            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            productRepository.save(product5);
        }
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    public List<ProductResponse> getProductBySkuCode(List<String> skuCode) {
        List<Product> products = productRepository.findBySkuCodeIn(skuCode);

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .skuCode(product.getSkuCode())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .build();
    }

}
