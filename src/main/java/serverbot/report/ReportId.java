package serverbot.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class ReportId implements Serializable {
    private LocalDateTime dateTime;
    private String userId;
    private String serverId;

    public ReportId(LocalDateTime dateTime, String userId, String serverId) {
        this.dateTime = dateTime;
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
        ReportId reportId = (ReportId) o;
        return Objects.equals( dateTime, reportId.dateTime ) &&
                Objects.equals( userId, reportId.userId ) &&
                Objects.equals( serverId, reportId.serverId );
    }

    @Override
    public int hashCode() {
        return Objects.hash( dateTime, userId, serverId );
    }
}
