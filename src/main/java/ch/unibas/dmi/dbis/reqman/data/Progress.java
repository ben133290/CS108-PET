package ch.unibas.dmi.dbis.reqman.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.UUID;

/**
 * TODO: write JavaDoc
 *
 * @author loris.sauter
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Progress {
  
  @Deprecated
  public static final double NO_POINTS = -999;
  
  public static final double NO_PROGRESS = -1d;
  
  private final UUID uuid;
  private double fraction = NO_PROGRESS;
  private UUID requirementUUID;
  private Date assessmentDate = null;
  private UUID progressSummaryUUID;
  private String comment;
  
  @Deprecated
  @JsonIgnore
  private String requirementName;
  @Deprecated
  @JsonIgnore
  private int milestoneOrdinal;
  @Deprecated
  @JsonIgnore
  private double points = 0;
  @Deprecated
  @JsonIgnore
  private double percentage = -1d;
  @Deprecated
  @JsonIgnore
  private Date date = null;
  
  public Progress() {
    uuid = UUID.randomUUID();
  }
  
  @Deprecated
  public Progress(String requirementName, int milestoneOrdinal, double points) {
    this();
    this.requirementName = requirementName;
    this.milestoneOrdinal = milestoneOrdinal;
    this.points = points;
  }
  
  @Deprecated
  public Progress(Requirement req) {
    this(req.getName(), req.getMinMilestoneOrdinal(), 0);
  }
  
  public Progress(Progress progress) {
    this();
    fraction = progress.getFraction();
    requirementUUID = progress.getRequirementUUID();
    assessmentDate = progress.getAssessmentDate();
    progressSummaryUUID = progress.getProgressSummaryUUID();
    comment = progress.getComment();
  }
  
  public double getFraction() {
    return fraction;
  }
  
  public void setFraction(double fraction) {
    this.fraction = fraction;
  }
  
  public UUID getRequirementUUID() {
    return requirementUUID;
  }
  
  public void setRequirementUUID(UUID requirementUUID) {
    this.requirementUUID = requirementUUID;
  }
  
  public Date getAssessmentDate() {
    return assessmentDate;
  }
  
  public void setAssessmentDate(Date assessmentDate) {
    this.assessmentDate = assessmentDate;
  }
  
  public UUID getProgressSummaryUUID() {
    return progressSummaryUUID;
  }
  
  public void setProgressSummaryUUID(UUID progressSummaryUUID) {
    this.progressSummaryUUID = progressSummaryUUID;
  }
  
  public String getComment() {
    return comment;
  }
  
  public void setComment(String comment) {
    this.comment = comment;
  }
  
  @Deprecated
  public Date getDate() {
    return date;
  }
  
  @Deprecated
  public void setDate(Date date) {
    this.date = date;
  }
  
  @Deprecated
  public double getPercentage() {
    return percentage;
  }
  
  /**
   * @param percentage
   * @deprecated Since the percentage / fraction is calculated while setting the points.
   */
  @Deprecated
  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }
  
  @Deprecated
  public String getRequirementName() {
    return requirementName;
  }
  
  @Deprecated
  public void setRequirementName(String requirementName) {
    this.requirementName = requirementName;
  }
  
  @Deprecated
  public int getMilestoneOrdinal() {
    return milestoneOrdinal;
  }
  
  @Deprecated
  public void setMilestoneOrdinal(int milestoneOrdinal) {
    this.milestoneOrdinal = milestoneOrdinal;
  }
  
  @Deprecated
  public double getPoints() {
    return points;
  }
  
  @Deprecated
  public void setPoints(double points) {
    this.points = points;
  }
  
  @Deprecated
  public void setPoints(double points, double max) {
    if (points == NO_POINTS) {
      percentage = 0d;
      this.points = -1;
      return;
    }
    
    this.points = points;
    if (Double.compare(0d, max) == 0 && Double.compare(0d, points) == 0) {
      // if max points == points == 0 -> progress 100%
      percentage = 1d;
    } else {
      percentage = points / max;
    }
  }
  
  /**
   * Returns whether this progress was freshly created.
   * A non-freshly created progress was either loaded from a group file
   * or was modified by a user.
   *
   * @return Whether this progress was freshly created. Then it returns {@code true}, {@code false} otherwise.
   */
  @JsonIgnore
  public boolean isFresh() {
    return fraction == NO_PROGRESS;
  }
  
  
  @Deprecated
  @JsonIgnore
  public double getPointsSensitive(Catalogue catalogue) {
    Requirement r = catalogue.getRequirementByName(requirementName);
    double factor = (r.isMalus() ? -1d : 1d) * percentage;
    return factor * r.getMaxPoints();
  }
  
  @JsonIgnore
  public boolean hasProgress() {
    return fraction > 0;
  }
  
  @Deprecated
  @JsonIgnore
  public boolean hasDefaultPercentage() {
    return Double.compare(-1d, percentage) == 0;
  }
  
  
  public UUID getUuid() {
    return uuid;
  }
  
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Progress{");
    sb.append("uuid=").append(uuid);
    sb.append(", fraction=").append(fraction);
    sb.append(", requirementUUID=").append(requirementUUID);
    sb.append(", assessmentDate=").append(assessmentDate);
    sb.append(", progressSummaryUUID=").append(progressSummaryUUID);
    sb.append(", comment='").append(comment).append('\'');
    sb.append('}');
    return sb.toString();
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    Progress progress = (Progress) o;
    
    return getUuid().equals(progress.getUuid());
  }
  
  @Override
  public int hashCode() {
    int result;
    long temp;
    result = getUuid().hashCode();
    temp = Double.doubleToLongBits(fraction);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (requirementUUID != null ? requirementUUID.hashCode() : 0);
    result = 31 * result + (assessmentDate != null ? assessmentDate.hashCode() : 0);
    result = 31 * result + (progressSummaryUUID != null ? progressSummaryUUID.hashCode() : 0);
    result = 31 * result + (comment != null ? comment.hashCode() : 0);
    return result;
  }
}