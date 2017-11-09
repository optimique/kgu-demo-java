package kgu.demo.mdb.filter;

import com.google.common.collect.Maps;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ApiKeyPreFilter extends ZuulFilter{

    @Value("${zuul.routes.THE-MOVIE-DB.api-key}")
    private String apiKey;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;  // run after PreDecoration
    }

    @Override
    public boolean shouldFilter() {

        RequestContext ctx = RequestContext.getCurrentContext();
        String uri = ctx.getRequest().getRequestURI();

        return !ctx.containsKey(FilterConstants.FORWARD_TO_KEY) // a filter has already forwarded
                && !ctx.containsKey(FilterConstants.SERVICE_ID_KEY) // a filter has already determined serviceId
                && uri.startsWith("/mdb-api/v3");
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Map<String, List<String>> qp = ctx.getRequestQueryParams();
        if (qp == null) {
            qp = Maps.newHashMap();
        }
        qp.put("api_key", Arrays.asList(apiKey));
        ctx.setRequestQueryParams(qp);

        //modify 결과는 SimpleHostRoutingFileter.buildHttpRequest() 에서 debug 해보면됨.

        return null;
    }
}