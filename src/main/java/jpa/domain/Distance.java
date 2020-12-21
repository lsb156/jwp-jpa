package jpa.domain;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.PositiveOrZero;

/**
 * @author : leesangbae
 * @project : jpa
 * @since : 2020-12-21
 */
@Embeddable
public class Distance {

    @PositiveOrZero
    private int distanceMeter;

    @OneToOne
    @JoinColumn(name = "distance_target_station_id")
    private Station distanceTargetStation;

    protected Distance() {
    }

    public Distance(int distanceMeter, Station distanceTargetStation) {
        this.distanceMeter = distanceMeter;
        this.distanceTargetStation = distanceTargetStation;
    }

    public Integer getDistanceMeter() {
        return distanceMeter;
    }

    public Station getDistanceTargetStation() {
        return distanceTargetStation;
    }
}
