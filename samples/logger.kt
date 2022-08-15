@file:Suppress("unused", "UNUSED_PARAMETER", "UNUSED_VARIABLE")

import org.roboquant.logging.MemoryLogger


fun memoryLogger() {
    // tag::standard[]
    val logger = MemoryLogger()
    val keys = logger.metricNames
    val metric = logger.getMetric(keys.first())
    // end::standard[]
}

