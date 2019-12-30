package writory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import writory.configuration.FqcnBeanNameGenerator

@ComponentScan(nameGenerator = FqcnBeanNameGenerator::class)
@SpringBootApplication
class WritoryApplication

fun main(args: Array<String>) {
    runApplication<WritoryApplication>(*args)
}
