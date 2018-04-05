package ch.unibas.dmi.dbis.reqman.analysis;

import ch.unibas.dmi.dbis.reqman.data.Milestone;
import ch.unibas.dmi.dbis.reqman.data.ProgressSummary;
import org.jetbrains.annotations.NotNull;

/**
 * TODO: Write JavaDoc
 *
 * @author loris.sauter
 */
public interface Filterable {
  
  void applyFilter(@NotNull Filter filter);
  
  void applyActiveMilestone(@NotNull Milestone ps);
  
  void clearFilter();
}
