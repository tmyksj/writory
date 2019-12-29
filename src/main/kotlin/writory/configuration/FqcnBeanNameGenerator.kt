package writory.configuration

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.AnnotationBeanNameGenerator

class FqcnBeanNameGenerator : AnnotationBeanNameGenerator() {

    override fun buildDefaultBeanName(definition: BeanDefinition): String {
        return definition.beanClassName ?: super.buildDefaultBeanName(definition)
    }

}
