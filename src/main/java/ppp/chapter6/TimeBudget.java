package ppp.chapter6;

import java.util.*;
import java.util.concurrent.*;

/**
 * 为一组任务设置时限. 在预定时间内请求旅游报价.
 */
public class TimeBudget {
    private static ExecutorService exec = Executors.newCachedThreadPool();

    public List<TravelQuote> getRankedTravelQuotes(TravelInfo travelInfo, Set<TravelCompany> companies,
                                                   Comparator<TravelQuote> ranking, long time, TimeUnit unit) throws InterruptedException {
        List<QuoteTask> tasks = new ArrayList<QuoteTask>();

        for (TravelCompany company : companies)
            tasks.add(new QuoteTask(company,travelInfo));

        List<Future<TravelQuote>> futures = exec.invokeAll(tasks, time, unit);//将多个任务提交到一个ExecutorService并获得结果
        List<TravelQuote> quotes = new ArrayList<TravelQuote>(tasks.size());//保存旅游报价结果

        //invokeAll按照任务集合中迭代器的顺序将所有的Future添加到返回的集合中，从而使调用者能将各个Future与其表示的Callable关联起来
        Iterator<QuoteTask> taskIterator = tasks.iterator();
        for (Future<TravelQuote> f : futures) {
            QuoteTask task = taskIterator.next();
            try {
                quotes.add(f.get());
            } catch (ExecutionException e) {
                quotes.add(task.getFailureQuote(e.getCause()));
            } catch (CancellationException e) {
                quotes.add(task.getTimeoutQuote(e));
            }
        }

        Collections.sort(quotes, ranking);
        return quotes;
    }
}

class QuoteTask implements Callable<TravelQuote> {
    private final TravelCompany company;
    private final TravelInfo travelInfo;

    public QuoteTask(TravelCompany company, TravelInfo travelInfo) {
        this.company = company;
        this.travelInfo = travelInfo;
    }

    TravelQuote getFailureQuote(Throwable t) {
        return null;
    }

    TravelQuote getTimeoutQuote(CancellationException e) {
        return null;
    }
    @Override
    public TravelQuote call() throws Exception {
        return company.solicitQuote(travelInfo);
    }
}

interface TravelCompany {
    TravelQuote solicitQuote(TravelInfo travelInfo) throws Exception;
}

interface TravelQuote {
}

interface TravelInfo {
}

