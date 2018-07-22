package sample.kstream.aggregation.kstreamaggregationsample1;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.annotations.KafkaStreamsProcessor;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.handler.annotation.SendTo;
import sample.kstream.aggregation.kstreamaggregationsample1.domain.FooDomain;
import sample.kstream.aggregation.kstreamaggregationsample1.domain.FooDomainStatusMonitor;

@SpringBootApplication
public class KstreamAggregationSample1Application {

	public static void main(String[] args) {
		SpringApplication.run(KstreamAggregationSample1Application.class, args);
	}

	@EnableBinding(KafkaStreamsProcessor.class)
	public static class KstreamAggregationSample1 {

		@StreamListener
		@SendTo("output")
		public KStream<String, FooDomain> process(@Input("input") KStream<String, FooDomain> inputStream) {

			JsonSerde<FooDomain> fooDomainJsonSerde = new JsonSerde<>(FooDomain.class);
			JsonSerde<FooDomainStatusMonitor> statusMonitorJsonSerde = new JsonSerde<>(FooDomainStatusMonitor.class);

			return inputStream
					// First thing to do is to re-key by the ID in the domain object
					.map((k, v) -> new KeyValue<>(v.getId(), v))
					// Then we group by the above re-keyed ID to geneate a grouped stream
					.groupByKey(Serialized.with(Serdes.String(), fooDomainJsonSerde))
					// Aggregate the values by their keyed group
					.aggregate(FooDomainStatusMonitor::new, //Initializer object
							//k - current key, v - current value, aggregate is the current state of the domain from any previous updates
							(k, v, aggregate) -> {
								// In the update status monitor, we check if the status changed from previous value
								aggregate.updateStatusMonitor(v);
								return aggregate;
							}, Materialized.with(Serdes.String(), statusMonitorJsonSerde))
					// Convert the resultant KTable above back to a KStream
					.toStream()
					// Filter only the ones whose status changed from active to pending
					.filter((k, v) -> v.isStatusChangedFromActiveToPending())
					// Extract the original domain object that matched the previous filter
					.map((k,v) -> new KeyValue<>(k, v.getFooDomain()));
		}
	}
}
