window.onload = function() {
    const pre = document.getElementsByTagName('pre');
    for (let i = 0; i < pre.length; i++) {
        const b = document.createElement('button');
        b.className = 'clipboard';
        b.textContent = 'Copy';
        if (pre[i].childNodes.length === 1 && pre[i].childNodes[0].nodeType === 3) {
            const div = document.createElement('div');
            div.textContent = pre[i].textContent;
            pre[i].textContent = '';
            pre[i].appendChild(div);
        }
        pre[i].appendChild(b);
    }
    const clipboard = new ClipboardJS('.clipboard', {
        target: function (b) {
            const p = b.parentNode;
            if (p.className.includes("highlight")) {
                const elems = p.getElementsByTagName("code");
                if (elems.length > 0)
                    return elems[0];
            }
            return p.childNodes[0];
        }
    });
    clipboard.on('success', function(e) {
        e.clearSelection();
        e.trigger.textContent = 'Copied';
        setTimeout(function() {
            e.trigger.textContent = 'Copy';
        }, 2000);
    });
    clipboard.on('error', function(e) {
        console.error('Action:', e.action, e);
        console.error('Trigger:', e.trigger);
    });
};
