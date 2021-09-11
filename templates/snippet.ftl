<pre class="kotlin">
<span class="pl-k">val</span> strategy <span class="pl-k">=</span> <span class="pl-en">EMACrossover</span>()
<span class="pl-k">val</span> metric <span class="pl-k">=</span> <span class="pl-en">AccountSummary</span>()
<span class="pl-k">val</span> experiment <span class="pl-k">=</span> <span class="pl-en">Experiment</span>(strategy, metric)

<span class="pl-k">val</span> feed <span class="pl-k">=</span> <span class="pl-en">CSVFeed</span>(<span class="pl-s"><span class="pl-pds">"</span>data/US<span class="pl-pds">"</span></span>)
experiment.<span class="pl-c1">run</span>(feed)
</pre>