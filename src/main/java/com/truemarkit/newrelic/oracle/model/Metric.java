package com.truemarkit.newrelic.oracle.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Holds data for metrics defined in metric.json config file.
 *
 * @author Dilip S Sisodia
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metric {
  private String id;
  private String sql;
  private String unit;
  private boolean enabled;
  private Integer descriptionColumnCount;
}
