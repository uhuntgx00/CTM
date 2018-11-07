package mil.ebs.ctm.news

import mil.ebs.ctm.news.NewsItem
import mil.ebs.ctm.Account

class Version
        extends NewsItem
{
    Integer xNumber
    NewsItem current
    Account author

    static mapping = {
        cache usage:'read-only'
    }

}