package br.com.lab.kotlin.ode.business.interactor

import kotlinx.coroutines.Job

internal class CompositeJobDisposable {
    private val jobs = linkedSetOf<Job>()

    @Synchronized
    fun add(job: Job?) {
        if (job == null) return
        purgeLocked()
        jobs.add(job)
        job.invokeOnCompletion {
            remove(job)
        }
    }

    @Synchronized
    fun remove(job: Job) {
        jobs.remove(job)
    }

    @Synchronized
    fun cancel() {
        val snapshot = jobs.toList()
        snapshot.forEach {
            if (it.isActive) {
                it.cancel()
            }
        }
        jobs.clear()
    }

    @Synchronized
    internal fun size(): Int {
        purgeLocked()
        return jobs.size
    }

    private fun purgeLocked() {
        jobs.removeAll { it.isCancelled || it.isCompleted }
    }
}
