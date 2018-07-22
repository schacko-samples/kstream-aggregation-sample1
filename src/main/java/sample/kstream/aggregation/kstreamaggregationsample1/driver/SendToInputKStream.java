package sample.kstream.aggregation.kstreamaggregationsample1.driver;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import sample.kstream.aggregation.kstreamaggregationsample1.domain.FooDomain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Soby Chacko
 */
public class SendToInputKStream {

	public static void main(String... args) {
		SpringApplication.run(SendToInputKStream.class, args);
	}

	@Bean
	public ApplicationRunner runner(KafkaTemplate<String, FooDomain> template) {
		return args -> {

			for (int i = 0; i < 100; i++) {
				FooDomain fooDomain = new FooDomain();
				fooDomain.setId("foo-" + i);
				fooDomain.setStatus("active");
				System.out.println("sending..." + i);
				kafkaTemplate().send("foo-data-in", "foo-" + i, fooDomain);
				Thread.sleep(1);
			}
		};
	}

	@Bean
	public ProducerFactory<String, FooDomain> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return props;
	}

	@Bean
	public KafkaTemplate<String, FooDomain> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
