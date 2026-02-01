(function () {
    function openModalWithHtml(html) {
        const body = document.getElementById('modal-body-content');
        const overlay = document.getElementById('allocation-modal');
        if (!body || !overlay) return;
        body.innerHTML = html;
        overlay.classList.add('show');
        const closeBtn = overlay.querySelector('.modal-close');
        closeBtn && closeBtn.focus();
    }

    function bindEllipsis() {
        document.querySelectorAll('.btn-details').forEach(btn => {
            btn.addEventListener('click', function () {
                const row = this.closest('tr');
                const allocationId = row ? row.getAttribute('data-id') : null;
                if (!allocationId) return;
                fetch(`/api/v1/allocations/${allocationId}`)
                    .then(r => r.json())
                    .then(a => {
                        let html = `<div><strong>Guest:</strong> ${a.fullName} <br/><strong>Email:</strong> ${a.email} <br/><strong>Address:</strong> ${a.address} <br/><strong>DOB:</strong> ${a.dob || ''}</div>`;
                        html += `<div style='margin-top:0.7em;'><strong>Room:</strong> ${a.room ? a.room.roomNumber : a.hostelRoomNumber} (${a.room ? a.room.roomType : 'N/A'})</div>`;
                        html += `<div><strong>Check In:</strong> ${a.checkIn} <br/><strong>Check Out:</strong> ${a.checkOut}</div>`;
                        html += `<div><strong>Identity Doc:</strong> ${a.identityDoc} <br/><strong>Payment:</strong> ${a.paymentMethod}</div>`;
                        openModalWithHtml(html);
                    });
            });
        });
    }

    function closeOnOverlayClick() {
        const overlay = document.getElementById('allocation-modal');
        if (!overlay) return;
        overlay.addEventListener('click', function (e) {
            if (e.target === overlay) closeModal();
        });
        overlay.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') closeModal();
            if (e.key === 'Tab') {
                const focusables = overlay.querySelectorAll('button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])');
                const first = focusables[0];
                const last = focusables[focusables.length - 1];
                if (e.shiftKey && document.activeElement === first) {
                    e.preventDefault();
                    last.focus();
                } else if (!e.shiftKey && document.activeElement === last) {
                    e.preventDefault();
                    first.focus();
                }
            }
        });
    }

    window.closeModal = function () {
        const overlay = document.getElementById('allocation-modal');
        if (!overlay) return;
        overlay.classList.remove('show');
        const body = document.getElementById('modal-body-content');
        if (body) body.innerHTML = '';
    };

    // prevent form submit if disabled (extra client-side guard)
    (function guardDisabledDelete() {
        const form = document.querySelector('form.confirm-delete[action*="/api/v1/hostels/"]');
        if (!form) return;
        const btn = form.querySelector('button[type="submit"]');
        form.addEventListener('submit', function (e) {
            if (btn && (btn.getAttribute('disabled') !== null || btn.getAttribute('aria-disabled') === 'true')) {
                e.preventDefault();
            }
        });
    })();

    // init
    bindEllipsis();
    closeOnOverlayClick();
})();
