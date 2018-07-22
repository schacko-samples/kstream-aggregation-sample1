package sample.kstream.aggregation.kstreamaggregationsample1.domain;

/**
 * @author Soby Chacko
 */
public class FooDomainStatusMonitor {

	boolean statusChangedFromActiveToPending;

	FooDomain fooDomain;

	public FooDomain getFooDomain() {
		return fooDomain;
	}

	public void setFooDomain(FooDomain fooDomain) {
		this.fooDomain = fooDomain;
	}

	public boolean isStatusChangedFromActiveToPending() {
		return statusChangedFromActiveToPending;
	}

	public void setStatusChangedFromActiveToPending(boolean statusChangedFromActiveToPending) {
		this.statusChangedFromActiveToPending = statusChangedFromActiveToPending;
	}

	public void updateStatusMonitor(FooDomain fooDomain) {
		System.out.println("In update status: " + fooDomain);
		if (getFooDomain() != null) {
			if (getFooDomain().getStatus().equals("active") &&
					fooDomain.getStatus().equals("pending")) {
				System.out.println("Got one active to pending..." + this.fooDomain + ":::" + fooDomain);
				this.statusChangedFromActiveToPending = true;
			}
			else if (getFooDomain().getStatus().equals("pending") &&
					fooDomain.getStatus().equals("active")) {
				System.out.println("Got one pending to active..." + this.fooDomain + ":::" + fooDomain);
				this.statusChangedFromActiveToPending = false;
			}
			else if (getFooDomain().getStatus().equals("pending")) {
				System.out.println("Got one already pending..." + this.fooDomain + ":::" + fooDomain);
				this.statusChangedFromActiveToPending = false;
			}
		}
		this.fooDomain = fooDomain;
	}
}
