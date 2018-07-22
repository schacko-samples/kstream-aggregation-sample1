package sample.kstream.aggregation.kstreamaggregationsample1.domain;

/**
 * @author Soby Chacko
 */
public class FooDomain {

	private String id;

	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString(){
		return "[FooDomain: " + "status: " + this.status + " id: " + this.id + "]";
	}
}
