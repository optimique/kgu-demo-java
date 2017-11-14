package kgu.demo.yts.filter;

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

    private Random randomGenerator;

    public OccasionalErrorPostFilter() {
        randomGenerator = new Random();
    }

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
                && uri.startsWith("/yts-api/v2");
    }

    @Override
    public Object run() {

        String envSuccessRate = env.getProperty("success-rate");

        boolean forcesFail = false;
        double successRate = 1.0;

        if (StringUtils.isNotBlank(envSuccessRate)) {
            try {
                double random = this.randomGenerator.nextDouble();
                successRate = Double.parseDouble(envSuccessRate);
                forcesFail = (successRate < random);
            } catch (Exception ignored) {
            }
        }

        if (forcesFail) {
            throw new RuntimeException("occasional error, success-rate=" + successRate);
        }

        return null;
    }
}
