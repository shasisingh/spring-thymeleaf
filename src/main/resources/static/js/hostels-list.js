(function () {
    const input = document.getElementById('hostel-search');
    const table = document.getElementById('hostels-table');
    if (!input || !table) return;
    const rows = Array.from(table.querySelectorAll('tbody tr'));
    let timer;
    const norm = s => (s || '').toLowerCase().trim();
    const queryMatch = (row, q) => {
        if (!q) return true;
        const name = norm(row.querySelector('[data-col="name"]')?.textContent);
        const addr = norm(row.querySelector('[data-col="address"]')?.textContent);
        return name.includes(q) || addr.includes(q);
    };

    function filter() {
        const q = norm(input.value);
        rows.forEach(row => {
            row.style.display = queryMatch(row, q) ? '' : 'none';
        });
    }

    input?.addEventListener('input', () => {
        clearTimeout(timer);
        timer = setTimeout(filter, 120);
    });
    // shrink/expand toggle
    const toggle = document.querySelector('.search-inline');
    const trigger = document.querySelector('.search-inline-trigger');

    function setOpen(v) {
        toggle.classList.toggle('open', !!v);
        trigger?.setAttribute('aria-expanded', !!v);
        if (v) input?.focus();
    }

    trigger?.addEventListener('click', () => setOpen(!toggle.classList.contains('open')));
    document.addEventListener('click', (e) => {
        if (!toggle?.contains(e.target) && toggle?.classList.contains('open')) setOpen(false);
    });
})();
