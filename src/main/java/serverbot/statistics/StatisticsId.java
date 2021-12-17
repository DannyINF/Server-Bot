package serverbot.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class StatisticsId implements Serializable {
    private String userId;
    private String serverId;

    public StatisticsId(String userId, String serverId) {
        this.userId = userId;
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        StatisticsId statisticsId = (StatisticsId) o;
        return Objects.equals( userId, statisticsId.userId ) &&
                Objects.equals( serverId, statisticsId.serverId );
    }

    @Override
    public int hashCode() {
        return Objects.hash( userId, serverId );
    }
}
