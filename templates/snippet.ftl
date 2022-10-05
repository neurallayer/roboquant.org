<pre class="kotlin mt-2">
<span class="pl-k">val</span> strategy <span class="pl-k">=</span> <span class="pl-en">EMACrossover</span>()
<span class="pl-k">val</span> metric <span class="pl-k">=</span> <span class="pl-en">AccountSummary</span>()
<span class="pl-k">val</span> roboquant <span class="pl-k">=</span> <span class="pl-en">Roboquant</span>(strategy, metric)

<span class="pl-k">val</span> feed <span class="pl-k">=</span> <span class="pl-en">CSVFeed</span>(<span class="pl-s"><span class="pl-pds">"</span>data/US<span class="pl-pds">"</span></span>)
roboquant.<span class="pl-c1">run</span>(feed)
</pre>