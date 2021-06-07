package ru.dreamworkerln.spring.utils.common.configurations.bpp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import ru.dreamworkerln.spring.utils.common.configurations.annotations.AutowireClassList;


import javax.annotation.PostConstruct;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Slf4j
public class InjectClassListBPP implements BeanPostProcessor, Ordered {

    @Autowired
    ApplicationContext context;

    @PostConstruct
    private void postConstruct() {
        log.trace("InjectBeanListBPP postConstruct");
    }

    /**
     * Этот метод будет вызываться над каждым бином
     */
    @SneakyThrows
    @Override
    public Object postProcessBeforeInitialization(Object bean, @NonNull String beanName) throws BeansException {


        // Получаем все поля всех бинов (включая суперклассы)
        // т.к. аннотация может присутствовать на protected поле предка
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = bean.getClass(); c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }

        for (Field field : fields) {

            List<Object> list;

            // Нашли List, куда нужно внедрять бины
            AutowireClassList autowireList = field.getAnnotation(AutowireClassList.class);
            if (autowireList != null) {

                // Читаем значения классов для List из аннотации AutowireList([Classes ...]) ---------------------------
                Class<?>[] classes = autowireList.value();
                list = Arrays.stream(classes)
                    .map(aClass -> Introspector.decapitalize(aClass.getSimpleName()))
                    .map(name -> context.getBean(name))
                    .collect(Collectors.toList());

                field.setAccessible(true);
                field.set(bean, list);
            }
        }
        return bean;
    }



    @Override
    public int getOrder() {
        return 0;
    }
}
