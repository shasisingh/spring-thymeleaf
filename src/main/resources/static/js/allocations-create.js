(function () {
  const form = document.getElementById('allocationForm');
  if (!form) return;
  const preview = document.getElementById('previewCard');
  const btn = document.getElementById('previewBtn');
  const closePreview = document.getElementById('closePreview');
  const get = id => document.getElementById(id);
  const fields = {
    prevName: () => form.querySelector('[name="fullName"]').value,
    prevEmail: () => form.querySelector('[name="email"]').value,
    prevAddress: () => form.querySelector('[name="address"]').value,
    prevIdDoc: () => form.querySelector('[name="identityDoc"]').value,
    prevPay: () => form.querySelector('[name="paymentMethod"]').value,
    prevHostel: () => form.querySelector('#hostelSelect').selectedOptions[0]?.textContent || '',
    prevRoom: () => form.querySelector('[name="hostelRoomNumber"]').value,
    prevCheckIn: () => form.querySelector('[name="checkIn"]').value,
    prevCheckOut: () => form.querySelector('[name="checkOut"]').value
  };

  function updatePreview() {
    Object.keys(fields).forEach(id => {
      const v = fields[id]();
      get(id).textContent = v || '—';
    });
  }

  function trapFocus(container) {
    const focusables = container.querySelectorAll('button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])');
    const first = focusables[0];
    const last = focusables[focusables.length - 1];

    function handleTab(e) {
      if (e.key !== 'Tab') return;
      if (e.shiftKey) {
        if (document.activeElement === first) {
          last.focus();
          e.preventDefault();
        }
      } else {
        if (document.activeElement === last) {
          first.focus();
          e.preventDefault();
        }
      }
    }

    container.addEventListener('keydown', handleTab);
    return () => container.removeEventListener('keydown', handleTab);
  }

  let releaseTrap = null;

  btn?.addEventListener('click', () => {
    updatePreview();
    const show = preview.style.display === 'none';
    preview.style.display = show ? 'block' : 'none';
    if (show) {
      preview.setAttribute('aria-hidden', 'false');
      releaseTrap = trapFocus(preview);
      preview.querySelector('#closePreview').focus();
    } else {
      preview.setAttribute('aria-hidden', 'true');
      if (releaseTrap) releaseTrap();
      btn.focus();
    }
  });
  closePreview?.addEventListener('click', () => {
    preview.style.display = 'none';
    preview.setAttribute('aria-hidden', 'true');
    if (releaseTrap) releaseTrap();
    btn.focus();
  });

  function toLocalDatetimeValue(date) {
    const pad = n => String(n).padStart(2, '0');
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
  }

  function formatDdMm(dateStr) {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    const pad = n => String(n).padStart(2, '0');
    return `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}`;
  }

  fields.prevCheckIn = () => formatDdMm(form.querySelector('[name="checkIn"]').value);
  fields.prevCheckOut = () => formatDdMm(form.querySelector('[name="checkOut"]').value);

  const hostelSelect = document.getElementById('hostelSelect');
  const roomSelect = document.getElementById('roomSelect');
  const hiddenRoomId = document.getElementById('roomIdInput');
  const roomSpinner = document.getElementById('roomSpinner');
  const roomSelectOverlay = document.getElementById('roomSelectOverlay');
  const selectedHostelId = window.__selectedHostelId || null;

  async function loadRoomsForHostel(hostelId) {
    roomSelect.innerHTML = '<option value="" disabled selected>Loading...</option>';
    roomSelect.disabled = true;
    if (roomSpinner) roomSpinner.style.display = 'inline-block';
    if (roomSelectOverlay) roomSelectOverlay.style.display = 'inline-block';
    try {
      const res = await fetch(`/api/v1/hostels/${hostelId}/rooms`);
      if (!res.ok) throw new Error(`Failed to load rooms (${res.status})`);
      const rooms = await res.json();
      roomSelect.innerHTML = '<option value="" disabled selected>Select room</option>';
      if (Array.isArray(rooms) && rooms.length > 0) {
        rooms.forEach(room => {
          const opt = document.createElement('option');
          const type = room.roomType ? ` (${room.roomType})` : '';
          opt.value = room.roomNumber;
          opt.textContent = `${room.roomNumber}${type}`;
          opt.dataset.roomId = String(room.id);
          roomSelect.appendChild(opt);
        });
        const preselectId = hiddenRoomId?.value;
        if (preselectId) {
          const match = Array.from(roomSelect.options).find(o => o.dataset.roomId === String(preselectId));
          if (match) match.selected = true;
        }
        roomSelect.disabled = false;
      } else {
        roomSelect.innerHTML = '<option value="" disabled selected>No rooms available</option>';
        roomSelect.disabled = true;
        hiddenRoomId.value = '';
      }
    } catch (e) {
      roomSelect.innerHTML = '<option value="" disabled selected>Error loading rooms</option>';
      roomSelect.disabled = true;
      hiddenRoomId.value = '';
      console.error(e);
    } finally {
      if (roomSpinner) roomSpinner.style.display = 'none';
      if (roomSelectOverlay) roomSelectOverlay.style.display = 'none';
    }
  }

  if (hostelSelect && roomSelect) {
    hostelSelect.addEventListener('change', function () {
      const hostelId = this.value;
      if (hostelId) {
        loadRoomsForHostel(hostelId);
      } else {
        roomSelect.innerHTML = '<option value="" disabled selected>Select room</option>';
        roomSelect.disabled = true;
        hiddenRoomId.value = '';
      }
    });

    roomSelect.addEventListener('change', function () {
      const selectedOption = roomSelect.selectedOptions[0];
      hiddenRoomId.value = (selectedOption && selectedOption.dataset && selectedOption.dataset.roomId) ? selectedOption.dataset.roomId : '';
    });

    const initialHostelVal = selectedHostelId || hostelSelect.value;
    if (initialHostelVal) {
      hostelSelect.value = String(initialHostelVal);
      loadRoomsForHostel(initialHostelVal);
    }
  }

  const checkInEl = form.querySelector('[name="checkIn"]');
  const checkOutEl = form.querySelector('[name="checkOut"]');
  if (checkInEl && !checkInEl.value) {
    checkInEl.value = toLocalDatetimeValue(new Date());
  }
  if (checkOutEl && !checkOutEl.value) {
    const d = new Date();
    d.setDate(d.getDate() + 2);
    checkOutEl.value = toLocalDatetimeValue(d);
  }

  const dobEl = form.querySelector('[name="dob"]');
  if (dobEl && !dobEl.value) {
    const d = new Date();
    d.setFullYear(d.getFullYear() - 18);
    const pad = n => String(n).padStart(2, '0');
    dobEl.value = `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
  }

  const saveBtn = document.getElementById('saveBtn');
  form.addEventListener('submit', function () {
    saveBtn.disabled = true;
  });
})();
