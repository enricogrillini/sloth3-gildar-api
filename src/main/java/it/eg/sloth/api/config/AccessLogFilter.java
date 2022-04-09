package it.eg.sloth.api.config;

import it.eg.sloth.api.filter.AccessLogBaseFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class AccessLogFilter extends AccessLogBaseFilter {
}
