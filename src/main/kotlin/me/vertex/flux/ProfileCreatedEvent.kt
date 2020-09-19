package me.jtux.flux

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import reactor.core.publisher.FluxSink
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.function.Consumer

class ProfileCreatedEvent(profile: Profile) : ApplicationEvent(profile)

@Component
class ProfileCreatedEventPublisher(val executor: Executor) : ApplicationListener<ProfileCreatedEvent>, Consumer<FluxSink<ProfileCreatedEvent>> {

    val queue = LinkedBlockingQueue<ProfileCreatedEvent>()

    override fun onApplicationEvent(event: ProfileCreatedEvent) {
        queue.offer(event)
    }

    override fun accept(sink: FluxSink<ProfileCreatedEvent>) {
        executor.execute {
            while (true) {
                try {
                    sink.next(queue.take())
                } catch (ex: InterruptedException) {
                    ReflectionUtils.rethrowRuntimeException(ex)
                }
            }
        }
    }

}