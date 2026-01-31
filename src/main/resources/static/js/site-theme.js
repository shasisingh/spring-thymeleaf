(function () {
    const root = document.documentElement;
    const themeMeta = document.querySelector('meta[name="theme-color"]');
    const maskIcon = document.getElementById('maskIcon');

    function applyThemeColor(current) {
        const color = current === 'dark' ? '#1a1d24' : '#7d6df0';
        if (themeMeta) themeMeta.setAttribute('content', color);
        if (maskIcon) maskIcon.setAttribute('color', current === 'dark' ? '#ffffff' : '#7d6df0');
    }

    function applyFavicon(current) {
        const href = current === 'dark' ? '/images/hostel-dark.svg' : '/images/hostel.svg';
        const head = document.getElementsByTagName('head')[0];
        // Replace only our dynamic icons, keep media-based fallbacks intact
        const oldIcon = document.getElementById('favicon');
        const oldShortcut = document.getElementById('faviconShortcut');
        if (oldIcon) oldIcon.parentNode.removeChild(oldIcon);
        if (oldShortcut) oldShortcut.parentNode.removeChild(oldShortcut);
        const icon = document.createElement('link');
        icon.id = 'favicon';
        icon.rel = 'icon';
        icon.type = 'image/svg+xml';
        icon.setAttribute('sizes', 'any');
        icon.href = href + '?v=' + current + '-' + Date.now();
        head.appendChild(icon);
        const shortcut = document.createElement('link');
        shortcut.id = 'faviconShortcut';
        shortcut.rel = 'shortcut icon';
        shortcut.type = 'image/svg+xml';
        shortcut.setAttribute('sizes', 'any');
        shortcut.href = icon.href;
        head.appendChild(shortcut);
    }

    function apply(current) {
        applyThemeColor(current);
        applyFavicon(current);
    }

    // Determine initial theme: stored -> system preference -> light
    let initial = localStorage.getItem('theme');
    if (!initial) {
        try {
            if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
                initial = 'dark';
            }
        } catch (_) {
        }
    }
    if (!initial) initial = 'light';
    root.setAttribute('data-theme', initial);
    apply(initial);

    function toggleTheme() {
        const current = root.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
        root.setAttribute('data-theme', current);
        localStorage.setItem('theme', current);
        apply(current);
        setTimeout(() => apply(current), 75);
    }

    window.__toggleTheme = toggleTheme;

    function markActiveLinks() {
        const path = location.pathname.replace(/\/$/, ''); // trim trailing slash
        document.querySelectorAll('.footer .actions a').forEach(a => {
            try {
                const href = new URL(a.getAttribute('href'), location.origin).pathname.replace(/\/$/, '');
                if (href === path) {
                    a.classList.add('active');
                } else {
                    a.classList.remove('active');
                }
            } catch (_) {
            }
        });
    }

    window.addEventListener('DOMContentLoaded', markActiveLinks);
})();
