package kgu.demo.mdb.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class OccasionalErrorPostFilter extends ZuulFilter {

    @Autowired
    private Environment env;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
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
        if( ctx.getResponseStatusCode() == 429 ) {
            longTask();
            ctx.setResponseStatusCode( 500 );
            ctx.setResponseBody("{\"status\":\"no limit\"}");
            return null;
        }

        String envSuccessRate = env.getProperty("success-rate");

        boolean forcesFail = false;
        double successRate = 1.0;

        if (StringUtils.isNotBlank(envSuccessRate)) {
            try {
                Random randomGenerator = new Random();
                double random = randomGenerator.nextDouble();
                successRate = Double.parseDouble(envSuccessRate);
                forcesFail = (successRate < random);
            } catch (Exception ignored) {
            }
        }

        if (forcesFail) {
            ctx.setResponseStatusCode(500);
            ctx.setResponseBody("{\"status\":\"error\"}");
//            throw new RuntimeException("occasional error, success-rate=" + successRate);
        }

        return null;
    }

    private void longTask() {
        // no action
    }
}
