package org.openmrs.module.chaiemr.reporting.cohort.definition;

import org.openmrs.module.reporting.cohort.definition.BaseCohortDefinition;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * RDQA cohort sample frame definition
 */
@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
@Localized("reporting.RDQACohortSampleFrameDefinition")
public class RDQACohortSampleFrameDefinition extends BaseCohortDefinition {

}
