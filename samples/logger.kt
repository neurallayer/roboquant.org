import org.roboquant.logging.MemoryLogger


fun memoryLogger() {
    // tag::standard[]
    val logger = MemoryLogger()
    val keys = logger.metricNames
    val data = logger.getMetric(keys.first())
    // end::standard[]
}

